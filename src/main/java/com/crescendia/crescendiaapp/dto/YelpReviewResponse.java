package com.crescendia.crescendiaapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class YelpReviewResponse {

	private List<ReviewDto> reviews;
	
	@JsonProperty("possible_languages")
	private List<String> possibleLanguages;
}
