package com.crescendia.crescendiaapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class GoogleVisionResponse {
	
	private List<VisionResponse> responses;
}
