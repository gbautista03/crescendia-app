package com.crescendia.crescendiaapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class VisionFaceAnnotation {
	
	private BoundingPoly boundingPoly;
	
	private FdBoundingPoly fdBoundingPoly;
	
	private List<Landmark> landmarks = null;
	
	private Float rollAngle;
	
	private Float panAngle;
	
	private Float tiltAngle;
	
	private Float detectionConfidence;
	
	private Float landmarkingConfidence;
	
	private String joyLikelihood;
	
	private String sorrowLikelihood;

	private String angerLikelihood;

	private String surpriseLikelihood;

	private String underExposedLikelihood;

	private String blurredLikelihood;

	private String headwearLikelihood;
}
