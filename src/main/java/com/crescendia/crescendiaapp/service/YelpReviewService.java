package com.crescendia.crescendiaapp.service;

import com.crescendia.crescendiaapp.dto.YelpAutocompleteResponse;
import com.crescendia.crescendiaapp.dto.YelpBusinessResponse;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;

public interface YelpReviewService {
	String LAT_MAKATI = "14.5598884";
	String LNG_MAKATI = "121.01565810000001";
	
	YelpReviewResponse fetchYelpReview(String id);
	
	YelpAutocompleteResponse searchYelpAutocomplete(String restaurantName);
	
	YelpBusinessResponse fetchBusiness(String id);
}
