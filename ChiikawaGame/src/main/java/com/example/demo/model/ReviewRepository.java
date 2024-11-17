package com.example.demo.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Reviews, Integer> {

	Page<Reviews> findByReviewStatus(Integer reviewStatus, Pageable pageable);

}
