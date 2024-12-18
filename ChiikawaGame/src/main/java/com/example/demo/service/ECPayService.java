package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ECPayService {
	
	
    @Autowired
    private OrderRepository orderRepository;

	public String ecpayCheckout(Long orderId) {
		
        // 1. 根據 orderId 查找訂單
        Order order = orderRepository.findById(orderId)
                                      .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // 3. 取得訂單總金額
        String orderTotal = order.getOrderTotal().toString(); // 轉換為字串，假設 orderTotal 是 BigDecimal
        orderTotal = String.valueOf(order.getOrderTotal().setScale(0, RoundingMode.HALF_UP).intValue()); //因為金額有小數，所以要四捨五入到整數


        // 4. 取得當前時間並格式化為指定格式
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String currentDateTime = now.format(formatter);

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		// 忽略的付款方式
		obj.setIgnorePayment("WebATM#ATM#BNPL#ApplePay#TWQR");
		obj.setMerchantTradeDate(currentDateTime);
		obj.setTotalAmount(orderTotal);
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		
		
		//若不回傳額外的付款資訊時，參數值請傳：Ｎ；
		//若要回傳額外的付款資訊時，參數值請傳：Ｙ ，付款完成後綠界後端會以POST方式回傳額外付款資訊到特店的ReturnURL 與OrderResultURL。
		obj.setNeedExtraPaidInfo("Y");
		
		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
		obj.setReturnURL("http://localhost:8080/ecpay/ecpayCheckout");
		
		//用POST方法導回商店，要在後端寫Redirect導回映射
//		obj.setOrderResultURL("http://localhost:8080/ecpay/ecpayCheckout");
		
		// 用"返回商店"的按鈕導回商店 ("輸入映射網頁的網址")；若OrderResultURL參數為空，則會使用此方式
		obj.setClientBackURL("http://localhost:8080/ecpay/showECPay");
		
		String form = all.aioCheckOut(obj, null);

		return form;
	}
	
    // 付款後更新成"已付款"
    // 更新付款狀態，返回更新結果的字符串
    public String updatePaymentStatus(Long orderId) {
        // 查詢指定的訂單
        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);
        
        // 檢查訂單是否存在
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            
            // 更新付款狀態為 'Paid'
            order.setPaymentStatus("Paid");

            // 保存訂單的變更
            orderRepository.save(order);
            return "Payment status updated to 'Paid' for order ID " + orderId; // 返回成功訊息
        } else {
            return "Order not found with ID " + orderId; // 返回錯誤訊息
        }
    }
	
}
