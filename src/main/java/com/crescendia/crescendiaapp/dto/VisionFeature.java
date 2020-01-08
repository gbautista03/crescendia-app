package com.crescendia.crescendiaapp.dto;

import com.crescendia.crescendiaapp.enums.VisionFeatureType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisionFeature {
	
	private VisionFeatureType type;
	
	private Integer maxResults;
}
