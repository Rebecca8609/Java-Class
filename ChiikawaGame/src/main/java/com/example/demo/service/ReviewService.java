package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Reviews;
import com.example.demo.model.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	//所有資料分頁Page
	public Page<Reviews> findReviewsByPage(Integer pageNumber){
		PageRequest pgr = PageRequest.of(pageNumber-1, 5,Sort.Direction.DESC ,"reviewDate");
		Page<Reviews> page = reviewRepo.findAll(pgr);
		return page != null ? page : Page.empty();  // 保證返回非空的 Page
	}
	
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
	
}
