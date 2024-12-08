package com.example.demo.model;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends JpaRepository<Reviews, Integer> {

	//用ReviewStatus找出評論
	Page<Reviews> findByReviewStatus(Integer reviewStatus, Pageable pageable);
    
    //模糊搜尋+日期範圍查詢+狀態
    @Query(value = "SELECT * FROM reviews r WHERE " +
            "(" +
            "(:reviewId IS NULL OR CAST(r.reviewId AS NVARCHAR) LIKE CONCAT('%', :reviewId, '%')) OR " +
            "(:reviewOrderId IS NULL OR CAST(r.reviewOrderId AS NVARCHAR) LIKE CONCAT('%', :reviewOrderId, '%')) OR " +
            "(:reviewComment IS NULL OR r.reviewComment LIKE CONCAT('%', :reviewComment, '%')) OR " +
            "(:reviewer IS NULL OR CAST(r.reviewer AS NVARCHAR) LIKE CONCAT('%', :reviewer, '%'))" +
            ")" +
            "AND (:reviewStatus IS NULL OR r.reviewStatus = :reviewStatus) " +
            "AND (:startDate IS NULL OR r.reviewDate >= :startDate) " +
            "AND (:endDate IS NULL OR r.reviewDate <= :endDate)", 
            nativeQuery = true)
    Page<Reviews> findByFiltersAndDateRange(
        @Param("reviewId") String reviewId,
        @Param("reviewOrderId") String reviewOrderId,
        @Param("reviewComment") String reviewComment,
        @Param("reviewer") String reviewer,
        @Param("reviewStatus") String reviewStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        Pageable pageable);

    //用BeReviewed和ReviewEvaluation找出出評論
    @Query(value = "SELECT * FROM reviews r WHERE r.beReviewed = :beReviewed AND (:reviewEvaluation IS NULL OR r.reviewEvaluation = :reviewEvaluation)", nativeQuery = true)
    Page<Reviews> findByBeReviewedAndReviewEvaluation(@Param("beReviewed") Integer beReviewed, 
    	    @Param("reviewEvaluation") Integer reviewEvaluation, 
    	    Pageable pageable);

}