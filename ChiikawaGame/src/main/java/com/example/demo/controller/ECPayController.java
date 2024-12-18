package com.example.demo.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Order;
import com.example.demo.service.ECPayService;
import com.example.demo.service.OrderService;

@Controller
public class ECPayController {

	@Autowired
	ECPayService ecpayService;
	
	@Autowired
	private OrderService orderService;
	
	// 映射: 顯示ECPay的網頁 
	// http://localhost:8080/ecpay/showECPay
	@GetMapping("/ecpay/showECPay")
	public String showECPay() {
	    return "ecpay/ecpayTest";
	}
	
	// API的網址:http://localhost:8080/ecpay/ecpayCheckout
	@PostMapping("/ecpay/ecpayCheckout")
	@ResponseBody
	public String ecpayCheckout(@RequestBody Map<String, String> request) {
		//前端請求參數(多+)
	    String deliveryMethod = request.get("deliveryMethod");
	    String paymentMethod = request.get("paymentMethod");
	    Long orderId = Long.parseLong(request.get("orderId")); // 這裡直接從 request 中獲取 orderId
	    
		
	    // 調用服務層邏輯生成 ECPay HTML 表單
		String aioCheckOutALLForm = ecpayService.ecpayCheckout(orderId);
	    // 更新訂單付款狀態
	    String paymentStatusUpdateMessage = ecpayService.updatePaymentStatus(orderId);
	    
	    // 在 ECPay 表單中包含付款狀態更新的結果 (如果需要)
//	    aioCheckOutALLForm += "<!-- " + paymentStatusUpdateMessage + " -->";  // 示例，將結果放入 HTML 注釋中

		return aioCheckOutALLForm;
	}
	

}
