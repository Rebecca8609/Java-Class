package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.ReviewPhotos;
import com.example.demo.model.ReviewPhotosRepository;
import com.example.demo.model.ReviewRepository;
import com.example.demo.model.Reviews;
import com.example.demo.service.ReviewService;

@Controller
public class ReviewManagementController {

	@Autowired
	private ReviewService rvwService;

	@Autowired
	private ReviewRepository rvwRepo;
	
	@Autowired
	private ReviewPhotosRepository rvwPhotosRepo;

	// 顯示後台評論頁面 http://localhost:8080/review/reviewManagement
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

	// 顯示前台上傳評論網頁 http://localhost:8080/review/addReview
	@GetMapping("/review/addReview")
	public String upload() {
		return "review/reviewInput";
	}

	// 前台上傳評論+多圖片
	@PostMapping("/review/addReviewPost")
	public String addReviewPost(@RequestParam MultipartFile[] reviewImg, int reviewEvaluation, String reviewComment,
			Integer reviewStatus, @RequestParam int reviewOrderId, Model model,RedirectAttributes redirectAttributes)
			throws IOException {
		
		Reviews reviews = new Reviews();
		reviewStatus = 1; //評論直接設定狀態1
		
		reviews.setReviewEvaluation(reviewEvaluation);
		reviews.setReviewComment(reviewComment);
		reviews.setReviewStatus(reviewStatus);
		reviews.setReviewOrderId(reviewOrderId);
		
		// 限制最多只能上傳 5 張圖片
		if (reviewImg.length > 5) {
			System.out.println("上傳超過5張圖片");
			return "review/reviewInput"; // 重新返回輸入頁面
		}

		//多圖片上傳
		List<ReviewPhotos> photoList = new ArrayList<>();
		for (MultipartFile onefile : reviewImg) {
			ReviewPhotos reviewPhotos = new ReviewPhotos();
			reviewPhotos.setReviewImg(onefile.getBytes());
			reviewPhotos.setReviews(reviews);
			photoList.add(reviewPhotos);
		}
		
		reviews.setReviewPhotos(photoList);
		rvwRepo.save(reviews);
		
		model.addAttribute("message", "送出成功！");
		return "review/reviewInput";
	}
	
	//處理對應reviewId的所有圖片請求
	//http://localhost:8080/review/getImagesByReviewId?p=35
	@GetMapping("/review/getImagesByReviewId")
	@ResponseBody
	public List<Map<String, Object>> getImagesByReviewId(@RequestParam(name = "p") Integer reviewId) {
	    List<ReviewPhotos> photos = rvwPhotosRepo.findByReviewsReviewId(reviewId); // 根據 reviewId 查找所有圖片
	    List<Map<String, Object>> imageDataList = new ArrayList<>();

	    for (ReviewPhotos photo : photos) {
	        // 將圖片轉換為 base64 編碼格式
	        String base64Image = Base64.getEncoder().encodeToString(photo.getReviewImg());

	        // 創建一個包含 base64 和 id 的 map
	        Map<String, Object> imageData = new HashMap<>();
	        imageData.put("imageBase64", base64Image);
	        imageData.put("reviewPhotoId", photo.getReviewPhotoId());
	        imageDataList.add(imageData);
	    }
	    return imageDataList; // 返回包含圖片 base64 編碼和 id 的對象列表
	}

	//處理刪除圖片
	@GetMapping("/review/delete")
	@ResponseBody
	public String deletePhotos(@RequestParam Integer id) {
	    try {
	        rvwService.deletePhototById(id); // 刪除圖片
	        return "success"; // 返回成功狀態
	    } catch (Exception e) {
	        return "error"; // 返回錯誤狀態
	    }
	}

	// 處理圖片顯示(目前沒用到)
	// http://localhost:8080/review/downloadImg?p=1
//	@GetMapping("/review/downloadImg")
//	public ResponseEntity<byte[]> downloadImg(@RequestParam(name = "p") Integer id) {
//		Optional<ReviewPhotos> op = rvwPhotosRepo.findById(id);
//
//		if (op.isPresent()) {
//			ReviewPhotos reviewPhotos = op.get();
//			byte[] photoByte = reviewPhotos.getReviewImg();
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.IMAGE_JPEG);
//			return new ResponseEntity<byte[]>(photoByte, headers, HttpStatus.OK);
//		}
//
//		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
//	}
	
	// 顯示前台賣場評價頁面 http://localhost:8080/review/reviewSeller
	@GetMapping("/review/reviewSeller")
	public String reviewSeller() {
	    return "review/reviewSeller";
	}
	
	// 利用beReviewed被評論者(賣家)找到該評論內容
	// http://localhost:8080/review/reviewPage?p=1&beReviewed=19910101
	@GetMapping("/review/reviewPage")
	@ResponseBody
	public Page<Reviews> rvwList(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(name = "beReviewed") Integer beReviewed,
	    @RequestParam(name = "reviewEvaluation", required = false) Integer reviewEvaluation
	) {
	    return rvwService.findReviewByBeReviewedAndEvaluation(beReviewed, reviewEvaluation, pageNumber);
	}
	
	//顯示ECPay的網頁 http://localhost:8080/showECPay
	@GetMapping("/showECPay")
	public String showECPay() {
	    return "ecpay/ecpayTest";
	}
        
	
}