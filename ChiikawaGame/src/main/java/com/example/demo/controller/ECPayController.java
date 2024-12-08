package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ECPayService;

@RestController
public class ECPayController {

	@Autowired
	ECPayService ecpayService;
	
	// API的網址:http://localhost:8080/ecpayCheckout
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(@RequestBody Map<String, String> request) {
		//前端請求參數(多+)
	    String deliveryMethod = request.get("deliveryMethod");
	    String paymentMethod = request.get("paymentMethod");
		
		String aioCheckOutALLForm = ecpayService.ecpayCheckout();

		return aioCheckOutALLForm;
	}
}
