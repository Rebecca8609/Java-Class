package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name="reviewPhotos")
public class ReviewPhotos {

	@Id @Column(name = "reviewPhotoId")
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	private int reviewPhotoId;
	
	@Lob
	@Column(name = "reviewImg")
	private byte[] reviewImg;
	
	@ManyToOne
	@JoinColumn(name="fk_reviews_id")
	private Reviews reviews;
	

	public ReviewPhotos() {
	}

	public int getReviewPhotoId() {
		return reviewPhotoId;
	}

	public void setReviewPhotoId(int reviewPhotoId) {
		this.reviewPhotoId = reviewPhotoId;
	}

	public byte[] getReviewImg() {
		return reviewImg;
	}

	public void setReviewImg(byte[] reviewImg) {
		this.reviewImg = reviewImg;
	}

	public Reviews getReviews() {
		return reviews;
	}

	public void setReviews(Reviews reviews) {
		this.reviews = reviews;
	}
	
	
}
