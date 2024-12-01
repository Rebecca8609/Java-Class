package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.ReviewRepository;
import com.example.demo.model.Reviews;
import com.example.demo.service.ReviewService;

@Controller
public class ReviewManagementController {

	@Autowired
	private ReviewService rvwService;

	@Autowired
	private ReviewRepository rvwRepo;

	// 顯示評論頁面 http://localhost:8080/review/reviewManagement
	@GetMapping("/review/reviewManagement")
	public String rvwList(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<Reviews> page = rvwService.findReviewsByPage(pageNumber);
		model.addAttribute("page", page); // 將 page 添加到 model 中
		return "review/reviewManagement";
	}

	// 狀態+模糊搜尋+日期搜尋(會先帶出全部的資料再根據搜尋顯示)
	// http://localhost:8080/search?p=1
	// http://localhost:8080/search?p=1&reviewStatus=2
	// http://localhost:8080/search?p=1&reviewId=20&reviewOrderId=20&reviewComment=20&reviewer=20
	// http://localhost:8080/search?p=1&reviewStatus=2&reviewId=20&reviewOrderId=20&reviewComment=20&reviewer=20
	// http://localhost:8080/search?p=1&reviewId=30&reviewOrderId=30&reviewComment=30&reviewer=30&startDate=2023-01-01&endDate=2023-12-30
	@ResponseBody
	@GetMapping("/review/search")
	public Page<Reviews> searchReviews(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(required = false) String reviewId,
	    @RequestParam(required = false) String reviewOrderId,
	    @RequestParam(required = false) String reviewComment,
	    @RequestParam(required = false) String reviewer,
	    @RequestParam(required = false) String reviewStatus,
	    @RequestParam(required = false) String startDate,
	    @RequestParam(required = false) String endDate) {

	    return rvwService.searchReviewsWithDateRange(pageNumber, reviewId, reviewOrderId, reviewComment, reviewer, reviewStatus, startDate, endDate);
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
			return ResponseEntity.status(404).body("找不到評論資料");
		}
	}

	// 上傳評論網頁
	// http://localhost:8080/review/addReview
	@GetMapping("/review/addReview")
	public String upload() {
		return "review/reviewInput";
	}

	// 上傳評論
	@PostMapping("/review/addReviewPost")
	public String addReviewPost(@RequestParam MultipartFile[] reviewImg, int reviewEvaluation, String reviewComment,
			Integer reviewStatus, @RequestParam(required = false) Optional<Integer> reviewOrderId, Model model)
			throws IOException {
		
		// 限制最多只能上傳 5 張圖片
		if (reviewImg.length > 5) {
			System.out.println("上傳超過5張圖片");
			return "review/reviewInput"; // 重新返回輸入頁面
		}

		reviewStatus = 1;
		int orderId = reviewOrderId.orElse(0); // 預設為 0

		//多圖片上傳
		ArrayList<Reviews> photoList = new ArrayList<>();
		for (MultipartFile onefile : reviewImg) {
			Reviews img = new Reviews();
			img.setReviewImg(onefile.getBytes());
			img.setReviewEvaluation(reviewEvaluation);
			img.setReviewComment(reviewComment);
			img.setReviewStatus(reviewStatus);
			img.setReviewOrderId(orderId);
			photoList.add(img);
		}
		rvwRepo.saveAll(photoList);
		model.addAttribute("message", "送出成功！");
		return "review/reviewInput";
		
		//單一圖片上傳
//	    byte[] imgByte = reviewImg.getBytes();
//	    img.setReviewImg(imgByte);
//	    img.setReviewEvaluation(reviewEvaluation);
//	    img.setReviewComment(reviewComment);
//	    img.setReviewStatus(reviewStatus);
//	    img.setReviewOrderId(reviewOrderId);
//	    
//	    rvwRepo.save(img);
//
//	    return "review/reviewInput";
	}

	// 處理圖片顯示
	// http://localhost:8080/review/downloadImg?p=31
	@GetMapping("/review/downloadImg")
	public ResponseEntity<byte[]> downloadImg(@RequestParam(name = "p") Integer id) {
		Optional<Reviews> op = rvwRepo.findById(id);

		if (op.isPresent()) {
			Reviews img = op.get();
			byte[] photoByte = img.getReviewImg();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<byte[]>(photoByte, headers, HttpStatus.OK);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}
}