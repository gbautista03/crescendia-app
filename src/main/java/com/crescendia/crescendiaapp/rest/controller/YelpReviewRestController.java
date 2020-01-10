package com.crescendia.crescendiaapp.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescendia.crescendiaapp.dto.YelpAutocompleteResponse;
import com.crescendia.crescendiaapp.dto.YelpBusinessResponse;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;
import com.crescendia.crescendiaapp.service.GoogleVisionService;
import com.crescendia.crescendiaapp.service.YelpReviewService;
import com.crescendia.crescendiaapp.util.WebPageScraper;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/yelp")
public class YelpReviewRestController {
	
	@Autowired
	private YelpReviewService yelpReviewService;
	
	@Autowired
	private GoogleVisionService googleVisionService;
	
	@GetMapping("/review/{businessId}")
	public ResponseEntity<YelpReviewResponse> fetchYelpReview(@PathVariable String businessId) {
		YelpBusinessResponse businessResponse = yelpReviewService.fetchBusiness(businessId);
		YelpReviewResponse yelpReviewScrape = WebPageScraper.scrapeWebPage(businessResponse.getUrl());
		
		return ok(googleVisionService.scanUserImage(yelpReviewScrape));
	}
	
	@GetMapping("/review")
	public ResponseEntity<YelpReviewResponse> searchYelpBusiness(@RequestParam(required = true) String restaurantName) {
		YelpAutocompleteResponse autocompleteResponse = yelpReviewService.searchYelpAutocomplete(restaurantName);
		if(autocompleteResponse != null) {
			YelpBusinessResponse businessResponse = yelpReviewService.fetchBusiness(autocompleteResponse.getBusinesses()
					.get(0).getId());
			YelpReviewResponse yelpReviewScrape = WebPageScraper.scrapeWebPage(businessResponse.getUrl());
			
			return ok(googleVisionService.scanUserImage(yelpReviewScrape));
		}
		
		return null;
	}
}
