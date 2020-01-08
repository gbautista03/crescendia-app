package com.crescendia.crescendiaapp.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.crescendia.crescendiaapp.config.GoogleVisionProperties;
import com.crescendia.crescendiaapp.dto.GoogleVisionPost;
import com.crescendia.crescendiaapp.dto.GoogleVisionResponse;
import com.crescendia.crescendiaapp.dto.VisionFaceAnnotation;
import com.crescendia.crescendiaapp.dto.VisionFeature;
import com.crescendia.crescendiaapp.dto.VisionImage;
import com.crescendia.crescendiaapp.dto.VisionImageSource;
import com.crescendia.crescendiaapp.dto.VisionRequest;
import com.crescendia.crescendiaapp.dto.VisionResponse;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;
import com.crescendia.crescendiaapp.enums.VisionFeatureType;
import com.crescendia.crescendiaapp.service.GoogleVisionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleVisionServiceImpl implements GoogleVisionService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GoogleVisionProperties googleVisionProperties;
	
	/**
	 * Process yelp reviewers avatar using google vision API
	 */
	public YelpReviewResponse scanUserImage(YelpReviewResponse yelpResponse) {
		UriComponents requestUri = UriComponentsBuilder.fromHttpUrl(googleVisionProperties.getAnnotateImageUrl())
				.queryParam("key", googleVisionProperties.getApiKey()).build();
		
		final HttpHeaders headers = new HttpHeaders();
		
		yelpResponse.getReviews().stream().forEach(r -> {
			String imageUri = r.getUser().getImageUrl();
			
			if(imageUri != null && !imageUri.isEmpty() && !imageUri.isBlank()) {
				VisionRequest visionRequest = new VisionRequest();
				
				VisionImageSource source = new VisionImageSource();
				source.setImageUri(imageUri);
				
				VisionImage visionImage = new VisionImage();
				visionImage.setSource(source);
				
				List<VisionFeature> featureList = Arrays.asList(new VisionFeature(VisionFeatureType.FACE_DETECTION,1));
				
				visionRequest.setImage(visionImage);
				visionRequest.setFeatures(featureList);
				
				GoogleVisionPost visionPost = new GoogleVisionPost(Arrays.asList(visionRequest));
			
				printRequest(visionPost);
				
				HttpEntity<GoogleVisionPost> httpEntity = new HttpEntity<>(visionPost, headers);
				
				ResponseEntity<GoogleVisionResponse> response = restTemplate.exchange(requestUri.toString(), HttpMethod.POST, httpEntity,
						new ParameterizedTypeReference<GoogleVisionResponse>() {});
				
				if(response != null) {
					GoogleVisionResponse visionResponse = response.getBody();
					
					if(visionResponse.getResponses() != null) {
						for(VisionResponse resp:visionResponse.getResponses()) {
							if(resp.getFaceAnnotations() != null) {
								for(VisionFaceAnnotation faceAnnotation:resp.getFaceAnnotations()) {
									r.getUser().setAngerLikelihood(faceAnnotation.getAngerLikelihood());
									r.getUser().setJoyLikelihood(faceAnnotation.getJoyLikelihood());
									r.getUser().setSorrowLikelihood(faceAnnotation.getSorrowLikelihood());
									r.getUser().setSurpriseLikelihood(faceAnnotation.getSurpriseLikelihood());
								}
							}
						}
					}
				}
			}
		});
		
		return yelpResponse;
	}
	
	private void printRequest(GoogleVisionPost visionPost) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(visionPost));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
