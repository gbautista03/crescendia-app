package com.crescendia.crescendiaapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("google.vision")
@Data
public class GoogleVisionProperties {
	
	private String annotateImageUrl;
	
	private String apiKey;
	
	private String processingMode;
}
