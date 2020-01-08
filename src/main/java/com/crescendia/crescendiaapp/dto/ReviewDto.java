package com.crescendia.crescendiaapp.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReviewDto {
	
	 private String id;
	 
	 private Integer rating;
	 
	 private UserDto user;
	 
	 private String text;
	 
	 @JsonProperty("time_created")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	 private Date timeCreated;
	 
	 private String url;
	 
	 @JsonFormat(pattern="yyyy-MM-dd")
	 private Date reviewDate;
}
