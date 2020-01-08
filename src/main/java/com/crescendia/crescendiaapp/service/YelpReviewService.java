package com.crescendia.crescendiaapp.service;

import com.crescendia.crescendiaapp.dto.YelpAutocompleteResponse;
import com.crescendia.crescendiaapp.dto.YelpBusinessResponse;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;

public interface YelpReviewService {
	String LAT_MAKATI = "14.5598884";
	String LNG_MAKATI = "121.01565810000001";
	
	/**
	 * Fetch yelp reviews for the given business id
	 * 
	 * @param	id
	 * 
	 * @return {@link com.crescendiaapp.dto.YelpReviewResponse}
	 */
	YelpReviewResponse fetchYelpReview(String id);
	
	/**
	 * Yelp search autocomplete if business ID is not available
	 * 
	 * @param	restaurantName
	 * 
	 * @return {@link com.crescendiaapp.dto.YelpAutocompleteResponse}
	 */
	YelpAutocompleteResponse searchYelpAutocomplete(String restaurantName);
	
	/**
	 * Query business after autocomplete search
	 * 
	 * @param	id
	 * 
	 * @return {@link com.crescendiaapp.dto.YelpBusinessResponse}
	 */
	YelpBusinessResponse fetchBusiness(String id);
}
