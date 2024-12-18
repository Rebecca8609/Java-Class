package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.ForumArticles;
import com.example.demo.model.ForumArticlesRepository;

@Service
public class ForumArticlesService {

	@Autowired
	private ForumArticlesRepository faRepo;
	
	public Page<ForumArticles> findForumArticlesByPage(Integer pageNumber){
		PageRequest pgr = PageRequest.of(pageNumber-1, 5,Sort.Direction.DESC ,"createdDate");
		Page<ForumArticles> page = faRepo.findAll(pgr);
		return page != null ? page : Page.empty();  // 保證返回非空的 Page
	}
}
