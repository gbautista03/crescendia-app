package com.crescendia.crescendiaapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class VisionRequest {

	private VisionImage image;
	
	private List<VisionFeature> features;
}
