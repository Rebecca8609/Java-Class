package com.example.demo.controller;

import java.io.IOException;
import java.util.Base64;
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
        model.addAttribute("page", page);  // 將 page 添加到 model 中
        return "review/reviewManagement";
    }

    // 取得所有評論的分頁資料 http://localhost:8080/api/reviews?p=2&status=1
    @GetMapping("/api/reviews")
    @ResponseBody
    public Page<Reviews> getReviews(
    		@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
    		@RequestParam(name = "status", required = false) Integer reviewStatus) {
        return rvwService.findReviewsByPageAndStatus(pageNumber, reviewStatus);
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
    //http://localhost:8080/review/upload
	@GetMapping("/review/upload")
	public String upload() {
		return "review/reviewInput";
	}
	
	@PostMapping("/review/uploadPost")
	public String uploadFilePost(@RequestParam MultipartFile file, Integer id, Model model) throws IOException {
	    byte[] fileByte = file.getBytes();
	    
	    Reviews photo = new Reviews();
	    photo.setReviewImg(fileByte);
	    
	    rvwRepo.save(photo);
	    model.addAttribute("okMsg", "上傳OK!");
	    
	    return "review/reviewInput";
	}
    
	@GetMapping("/review/download")
	public ResponseEntity<byte[]> downloadPhotos(@RequestParam Integer id) {
		Optional<Reviews> op = rvwRepo.findById(id);
		
		if(op.isPresent()) {
			Reviews photos = op.get();
			byte[] photoByte = photos.getReviewImg();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<byte[]>(photoByte, headers, HttpStatus.OK);
		}
		
		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

   
}