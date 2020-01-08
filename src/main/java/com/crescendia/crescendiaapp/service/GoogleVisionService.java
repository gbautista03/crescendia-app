package com.crescendia.crescendiaapp.service;

import com.crescendia.crescendiaapp.dto.YelpReviewResponse;

public interface GoogleVisionService {
	
	/**
	 * Process yelp reviewers avatar using google vision API
	 * 
	 * @param	yelpResponse
	 * 
	 * @return {@link com.crescendiaapp.dto.YelpAutocompleteResponse}
	 */
	YelpReviewResponse scanUserImage(YelpReviewResponse yelpResponse);
}
