package com.crescendia.crescendiaapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("yelp.rest")
@Data
public class YelpRestProperties {
	private String reviewUrl;
	
	private String businessUrl;
	
	private String autocomplete;
	
	private String apiKey;
}
