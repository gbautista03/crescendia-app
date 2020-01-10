package com.crescendia.crescendiaapp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.crescendia.crescendiaapp.dto.ReviewDto;
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
	
	private static final int BATCH_COUNT = 5;
	
	private static final String MODE = "batch";
	
	public YelpReviewResponse scanUserImage(YelpReviewResponse yelpResponse) {
		UriComponents requestUri = UriComponentsBuilder.fromHttpUrl(googleVisionProperties.getAnnotateImageUrl())
				.queryParam("key", googleVisionProperties.getApiKey()).build();
		
		if(MODE.equals(googleVisionProperties.getProcessingMode())) {
			// batch of 5
			List<VisionRequest> visionRequests = yelpResponse.getReviews().stream().map(this::createVisionRequest)
					.collect(Collectors.toList());
			
			List<VisionRequest> batchRequests = new ArrayList<>();
			
			int count = 0;
			for(VisionRequest request:visionRequests) {
				batchRequests.add(request); 
				count++;
				
				if(batchRequests.size() % BATCH_COUNT == 0) {
					GoogleVisionPost visionPost = new GoogleVisionPost(batchRequests);
					
					printRequest(visionPost);
					
					GoogleVisionResponse visionResponse = processVisionRequest(visionPost, requestUri.toString());
					
					if(visionResponse != null && visionResponse.getResponses() != null) {
						int index = count - BATCH_COUNT;
						for(VisionResponse resp:visionResponse.getResponses()) {
							if(resp.getFaceAnnotations() != null) {
								for(VisionFaceAnnotation faceAnnotation:resp.getFaceAnnotations()) {
									yelpResponse.getReviews().get(index).getUser()
										.setAngerLikelihood(faceAnnotation.getAngerLikelihood());
									yelpResponse.getReviews().get(index).getUser()
										.setJoyLikelihood(faceAnnotation.getJoyLikelihood());
									yelpResponse.getReviews().get(index).getUser()
										.setSorrowLikelihood(faceAnnotation.getSorrowLikelihood());
									yelpResponse.getReviews().get(index).getUser()
										.setSurpriseLikelihood(faceAnnotation.getSurpriseLikelihood());
								}
							}
							index++;
						}
					}
					batchRequests.clear();
				}
			}
		} else {
			// individual request
			yelpResponse.getReviews().stream().forEach(r -> {
				
				GoogleVisionPost visionPost = createVisionPost(r);
				
				if(visionPost != null) {
					GoogleVisionResponse visionResponse = processVisionRequest(visionPost, requestUri.toString());

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
			});
		}
		
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
	
	private GoogleVisionPost createVisionPost(ReviewDto review) {
		
		VisionRequest visionRequest = createVisionRequest(review);
		if(visionRequest != null) {
			GoogleVisionPost visionPost = new GoogleVisionPost(Arrays.asList(visionRequest));
		
			printRequest(visionPost);
			
			return visionPost;
		}
		
		return null;
	}
	
	private VisionRequest createVisionRequest(ReviewDto review) {
		String imageUri = review.getUser().getImageUrl();
		
		if(imageUri != null && !imageUri.isEmpty() && !imageUri.isBlank()) {
			VisionRequest visionRequest = new VisionRequest();

			VisionImageSource source = new VisionImageSource();
			source.setImageUri(imageUri);

			VisionImage visionImage = new VisionImage();
			visionImage.setSource(source);

			List<VisionFeature> featureList = Arrays.asList(new VisionFeature(VisionFeatureType.FACE_DETECTION,1));

			visionRequest.setImage(visionImage);
			visionRequest.setFeatures(featureList);
			
			return visionRequest;
		}
		
		return null;
	}
	
	private GoogleVisionResponse processVisionRequest(GoogleVisionPost visionPost, String requestUri) {
		final HttpHeaders headers = new HttpHeaders();
		HttpEntity<GoogleVisionPost> httpEntity = new HttpEntity<>(visionPost, headers);
		
		ResponseEntity<GoogleVisionResponse> response = restTemplate.exchange(requestUri.toString(), HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<GoogleVisionResponse>() {});
		
		return response.getBody();
	}
}
