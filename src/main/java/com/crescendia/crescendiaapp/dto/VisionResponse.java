package com.crescendia.crescendiaapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class VisionResponse {
	
	private List<VisionFaceAnnotation> faceAnnotations;
}
