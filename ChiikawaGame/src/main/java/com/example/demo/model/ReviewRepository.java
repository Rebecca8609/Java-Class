package com.example.demo.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends JpaRepository<Reviews, Integer> {

	Page<Reviews> findByReviewStatus(Integer reviewStatus, Pageable pageable);
	
    // 查詢日期範圍內的評論
    Page<Reviews> findByReviewDateBetween(Date startDate, Date endDate, Pageable pageable);

    // 同時根據 status 和日期範圍查詢
    Page<Reviews> findByReviewStatusAndReviewDateBetween(Integer reviewStatus, Date startDate, Date endDate, Pageable pageable);
    
    //模糊搜尋
    @Query(value = "SELECT * FROM reviews r WHERE " +
            "(:reviewId IS NULL OR CAST(r.reviewId AS NVARCHAR) LIKE CONCAT('%', :reviewId, '%')) OR " +
            "(:reviewOrderId IS NULL OR CAST(r.reviewOrderId AS NVARCHAR) LIKE CONCAT('%', :reviewOrderId, '%')) OR " +
            "(:reviewComment IS NULL OR r.reviewComment LIKE CONCAT('%', :reviewComment, '%')) OR " +
            "(:beReviewed IS NULL OR CAST(r.beReviewed AS NVARCHAR) LIKE CONCAT('%', :beReviewed, '%'))", 
    nativeQuery = true)
    Page<Reviews> findByFilters(
        @Param("reviewId") String reviewId,
        @Param("reviewOrderId") String reviewOrderId,
        @Param("reviewComment") String reviewComment,
        @Param("beReviewed") String beReviewed,
        Pageable pageable);

}