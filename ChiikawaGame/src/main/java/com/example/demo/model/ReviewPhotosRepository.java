package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotosRepository extends JpaRepository<ReviewPhotos, Integer> {
	public List<ReviewPhotos> findByReviewsReviewId(Integer reviewId);
	
}
