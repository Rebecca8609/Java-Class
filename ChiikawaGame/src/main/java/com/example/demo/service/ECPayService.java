package com.example.demo.service;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ECPayService {

	public String ecpayCheckout() {

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		// 忽略的付款方式
		obj.setIgnorePayment("WebATM#ATM#BNPL#ApplePay#TWQR");
		obj.setMerchantTradeDate("2024/12/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
//		obj.setReturnURL("<http://211.23.128.214:5000>");
		obj.setReturnURL("ok");
		obj.setNeedExtraPaidInfo("N");
		// 商店轉跳網址 (Optional)
//		obj.setClientBackURL("<http://192.168.1.37:8080/>");
		obj.setClientBackURL("http://localhost:8080/showECPay");
		String form = all.aioCheckOut(obj, null);

		return form;
	}
	
}
