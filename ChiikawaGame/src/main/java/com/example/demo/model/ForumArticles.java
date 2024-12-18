package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity @Table(name="forumArticles")
public class ForumArticles {
	
	@Id @Column(name = "articleId")
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	private int articleId;
	
	@Column(name = "articleTitle")
	private String articleTitle;
	
	@Column(name = "articleContent")
	private String articleContent;
	
	@Column(name = "articleStatus")
	private int articleStatus;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8") // Restful API的時間格式
	@Column(name = "createdDate")
	private Date createdDate;
	
	//要對接會員User的Id
	@Column(name = "articleAuthorId")
	private int articleAuthorId;
	
//	@JoinColumn(name = "articleAuthorId")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Users userId;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "forumArticles")
	private List<ForumArticlePhotos> forumArticlePhotos = new ArrayList<>();
	
	@PrePersist // 當物件要轉換成 Persistent 狀態以前，執行這個方法
	public void onCreate() {
		if (createdDate == null) {
			createdDate = new Date();
		}
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

	public int getArticleStatus() {
		return articleStatus;
	}

	public void setArticleStatus(int articleStatus) {
		this.articleStatus = articleStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getArticleAuthorId() {
		return articleAuthorId;
	}

	public void setArticleAuthorId(int articleAuthorId) {
		this.articleAuthorId = articleAuthorId;
	}
	
	
}
