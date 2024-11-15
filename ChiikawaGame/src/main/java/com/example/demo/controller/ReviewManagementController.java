package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Reviews;
import com.example.demo.service.ReviewService;

@Controller
public class ReviewManagementController {

    @Autowired
    private ReviewService rvwService;

    
    // 顯示評論頁面 http://localhost:8080/review/reviewManagement
    @GetMapping("/review/reviewManagement")
    public String rvwList(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model model) {
        Page<Reviews> page = rvwService.findReviewsByPage(pageNumber);
        model.addAttribute("page", page);  // 將 page 添加到 model 中
        return "review/reviewManagement";
    }

    // 取得所有評論的分頁資料 http://localhost:8080/api/reviews
    @GetMapping("/api/reviews")
    @ResponseBody
    public Page<Reviews> getReviews(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
        return rvwService.findReviewsByPage(pageNumber);
    }

    // 取得單一評論資料(給更新用)
    @GetMapping("/review/update")
    public String getReviewById(@RequestParam Integer id, Model model) {
        Reviews reviews = rvwService.findReviewById(id);
        if (reviews != null) {
            model.addAttribute("reviews", reviews);
            return "review/reviewManagement";
        } else {
            model.addAttribute("error", "Review not found");
            return "error";
        }
    }

    // 更新評論狀態
    @PostMapping("/review/updateStatus")
    @ResponseBody
    public ResponseEntity<String> updateReviewStatus(@RequestParam Integer id, @RequestParam Integer status) {
        Reviews updatedReview = rvwService.updateReviewStatusById(id, status);
        if (updatedReview != null) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(404).body("Review not found");
        }
    }
    
    // API-返回圖片資料
    @GetMapping("/review/image/{id}")
    public ResponseEntity<byte[]> getReviewImage(@PathVariable Integer id) {
        Reviews review = rvwService.findReviewById(id);
        if (review == null || review.getReviewImg() == null) {
            return ResponseEntity.notFound().build();
        }

        // 返回圖片資料
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg") // 根據資料類型調整
                .body(review.getReviewImg());
    }
    
}
