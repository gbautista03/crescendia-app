package com.crescendia.crescendiaapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDto {
	
	private String id;
	
	@JsonProperty("profile_url")
	private String profileUrl;
	
	@JsonProperty("image_url")
	private String imageUrl;
	
	private String name;
	
	private String joyLikelihood;
	
	private String sorrowLikelihood;
	
	private String angerLikelihood;
	
	private String surpriseLikelihood;
}
