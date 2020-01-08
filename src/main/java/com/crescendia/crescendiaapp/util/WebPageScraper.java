package com.crescendia.crescendiaapp.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crescendia.crescendiaapp.dto.ReviewDto;
import com.crescendia.crescendiaapp.dto.UserDto;
import com.crescendia.crescendiaapp.dto.YelpReviewResponse;

public class WebPageScraper {

	private static final String REVIEW_LI_CLASS = "lemon--li__373c0__1r9wz u-space-b3 u-padding-b3 border--bottom__373c0__uPbXS border-color--default__373c0__2oFDT";
	
	private static final String REVIEW_DATE_CLASS = "lemon--span__373c0__3997G text__373c0__2pB8f text-color--mid__373c0__3G312 text-align--left__373c0__2pnx_";
	
	private static final String REVIEW_USER_NAME = "lemon--span__373c0__3997G text__373c0__2pB8f fs-block text-color--inherit__373c0__w_15m text-align--left__373c0__2pnx_ text-weight--bold__373c0__3HYJa";
	
	private static final String REVIEW_USER_IMG = "lemon--img__373c0__3GQUb photo-box-img__373c0__O0tbt";

	public static YelpReviewResponse scrapeWebPage(String url) {
		YelpReviewResponse yelpReview = new YelpReviewResponse();
		yelpReview.setReviews(new ArrayList<ReviewDto>());
		yelpReview.setPossibleLanguages(new ArrayList<String>());
		try {
			Document doc = Jsoup.connect(url).get();

			Elements reviewElements = doc.getElementsByClass(REVIEW_LI_CLASS);

			for(Element reviewElement:reviewElements) {
				Elements reviewDateElements = reviewElement.getElementsByClass(REVIEW_DATE_CLASS);
				String reviewDate = reviewDateElements.get(0).text();
				
				Elements reviewTextElements = reviewElement.select("span[lang]");
				Element reviewTextElement = reviewTextElements.get(0);
				String reviewText = reviewTextElement.text();
				String language = reviewTextElement.attr("lang");
				
				Elements userNameElements = reviewElement.getElementsByClass(REVIEW_USER_NAME);
				String userName = userNameElements.get(0).text();
				
				Elements userImgUrl = reviewElement.getElementsByClass(REVIEW_USER_IMG).select("img[loading][srcSet]");
				String imageUrl = userImgUrl.get(0).attr("src");
				
				Elements reviewRatingElements = reviewElement.select("div[aria-label][role=img]");
				String rating = Character.toString(reviewRatingElements.get(0).attr("aria-label").charAt(0));
				
				UserDto user = new UserDto();
				user.setName(userName);
				user.setImageUrl(imageUrl);
				
				ReviewDto review = new ReviewDto();
				review.setRating(Integer.valueOf(rating));
				review.setText(reviewText);
				review.setUser(user);
				review.setReviewDate(new SimpleDateFormat("MM/dd/yyyy").parse(reviewDate));
				
				yelpReview.getReviews().add(review);
				
				if(!yelpReview.getPossibleLanguages().contains(language)) {
					yelpReview.getPossibleLanguages().add(language);
				}
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return yelpReview;
	}
}
