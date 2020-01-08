package com.crescendia.crescendiaapp.service;

import com.crescendia.crescendiaapp.dto.YelpReviewResponse;

public interface GoogleVisionService {
	
	YelpReviewResponse scanUserImage(YelpReviewResponse yelpResponse);
}
