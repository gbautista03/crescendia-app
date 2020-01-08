package com.crescendia.crescendiaapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleVisionPost {
	
	private List<VisionRequest> requests;
}
