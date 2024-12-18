package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name="forumArticlePhotos")
public class ForumArticlePhotos {

	@Id 
	@Column(name = "forumArticlePhotoId")
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	private int reviewPhotoId;
	
	@Column(name = "requestItemPhoto")
	private byte[] requestItemPhoto;
	
	@ManyToOne
	@JoinColumn(name="fk_forumArticles_id")
	private ForumArticles forumArticles;

	public ForumArticlePhotos() {
	}

	public int getReviewPhotoId() {
		return reviewPhotoId;
	}

	public void setReviewPhotoId(int reviewPhotoId) {
		this.reviewPhotoId = reviewPhotoId;
	}

	public byte[] getRequestItemPhoto() {
		return requestItemPhoto;
	}

	public void setRequestItemPhoto(byte[] requestItemPhoto) {
		this.requestItemPhoto = requestItemPhoto;
	}

	public ForumArticles getForumArticles() {
		return forumArticles;
	}

	public void setForumArticles(ForumArticles forumArticles) {
		this.forumArticles = forumArticles;
	}

	
}
