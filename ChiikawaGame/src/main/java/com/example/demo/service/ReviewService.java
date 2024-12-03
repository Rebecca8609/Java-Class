package com.example.demo.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Reviews;
import com.example.demo.model.ReviewPhotosRepository;
import com.example.demo.model.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private ReviewPhotosRepository reviewPhotosRepo;
	
	//所有資料分頁Page
	public Page<Reviews> findReviewsByPage(Integer pageNumber){
		PageRequest pgr = PageRequest.of(pageNumber-1, 5,Sort.Direction.DESC ,"reviewDate");
		Page<Reviews> page = reviewRepo.findAll(pgr);
		return page != null ? page : Page.empty();  // 保證返回非空的 Page
	}
	
	//用ID對應每筆資料
	public Reviews findReviewById(Integer id) {
		Optional<Reviews> op = reviewRepo.findById(id);
		
		if(op.isPresent()) {
			return op.get();
		}
		return null;
	}

	//修改評論的狀態
	@Transactional
	public Reviews updateReviewStatusById(Integer id, Integer rvwStatus) {
        Optional<Reviews> op = reviewRepo.findById(id);
		
		if(op.isPresent()) {
			Reviews rvw = op.get();
			rvw.setReviewStatus(rvwStatus);
			return rvw;
		}
		return null;
	}
    
    //狀態+模糊搜尋+日期範圍搜尋
    public Page<Reviews> searchReviewsWithDateRange(
            Integer pageNumber, String reviewId, String reviewOrderId, String reviewComment, String reviewer, String reviewStatus,
            String startDate, String endDate) {

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5, Sort.Direction.DESC, "reviewDate");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;

        try {
            if (startDate != null && !startDate.isEmpty()) {
                start = sdf.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = sdf.parse(endDate);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return Page.empty();
        }

        return reviewRepo.findByFiltersAndDateRange(reviewId, reviewOrderId, reviewComment, reviewer, reviewStatus, start, end, pageRequest);
    }
    
    //刪除圖片
	public void deletePhototById(Integer id) {
		reviewPhotosRepo.deleteById(id);
	}

}