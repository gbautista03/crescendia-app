package com.crescendia.crescendiaapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.crescendia.crescendiaapp.config.YelpRestProperties;
import com.crescendia.crescendiaapp.dto.YelpAutocompleteResponse;
import com.crescendia.crescendiaapp.dto.YelpBusinessResponse;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;
import com.crescendia.crescendiaapp.service.YelpReviewService;

@Service
public class YelpReviewServiceImpl implements YelpReviewService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private YelpRestProperties yelpRestProperties;
	
	public YelpReviewResponse fetchYelpReview(String id) {
		UriComponents requestUri = UriComponentsBuilder.fromHttpUrl(yelpRestProperties.getReviewUrl()).buildAndExpand(id);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, yelpRestProperties.getApiKey());
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<YelpReviewResponse> response = restTemplate.exchange(requestUri.toString(), HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<YelpReviewResponse>() {});
		return response.getBody();
	}
	
	public YelpAutocompleteResponse searchYelpAutocomplete(String restaurantName) {
		UriComponents requestUri = UriComponentsBuilder.fromHttpUrl(yelpRestProperties.getAutocomplete()).
				queryParam("latitude", LAT_MAKATI).queryParam("longitude", LNG_MAKATI).queryParam("text", restaurantName).
				build();
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, yelpRestProperties.getApiKey());
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<YelpAutocompleteResponse> response = restTemplate.exchange(requestUri.toString(), HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<YelpAutocompleteResponse>() {});
		return response.getBody();
	}
	
	public YelpBusinessResponse fetchBusiness(String id) {
		UriComponents requestUri = UriComponentsBuilder.fromHttpUrl(yelpRestProperties.getBusinessUrl()).buildAndExpand(id);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, yelpRestProperties.getApiKey());
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<YelpBusinessResponse> response = restTemplate.exchange(requestUri.toString(), HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<YelpBusinessResponse>() {});
		return response.getBody();
	}
}
