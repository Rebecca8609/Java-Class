package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.ForumArticles;
import com.example.demo.model.Reviews;
import com.example.demo.service.ForumArticlesService;

@Controller
public class ForumArticlesController {
	
	@Autowired
	private ForumArticlesService forumService;
	
	//　顯示文章頁面 http://localhost:8080/forum/forumArticlesManagement
	@GetMapping("/forum/forumArticlesManagement")
	public String forumArticleList() {
		return "forum/forumArticlesManagement";
	}
	
	// 取得所有評論的分頁資料(後端是否能正常抓取)
    // http://localhost:8080/api/forumArticles?p=1
	@GetMapping("/api/forumArticles")
	@ResponseBody
	public Page<ForumArticles> getForumArticles(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,Model model) {
		Page<ForumArticles> page = forumService.findForumArticlesByPage(pageNumber);
		model.addAttribute("page", page); // 將 page 添加到 model 中
		return forumService.findForumArticlesByPage(pageNumber);
	}
	
	
}
