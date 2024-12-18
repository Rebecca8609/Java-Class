package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.ShippingInfo;
import com.example.demo.service.ShippingInfoService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shipping")
public class ShippingInfoController {

    @Autowired
    private ShippingInfoService shippingInfoService;

    // 物流追蹤頁面
    @GetMapping("/shipping-tracking")
    public String adminShippingTracking() {
        return "adminShippingTracking"; // 對應 resources/templates/adminShippingTracking.html
    }

    // 返回 JSON 的方法，顯式加上 @ResponseBody
    @ResponseBody
    @GetMapping
    public List<Map<String, Object>> getOrderShippingInfo(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long buyerId) {
        return shippingInfoService.getOrderShippingInfo(orderId, status, buyerId);
    }

    // 更新物流信息，支持文件上傳
    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShippingInfo(
            @PathVariable Long id,
            @RequestPart("shippingInfo") ShippingInfo updatedInfo,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            boolean updated = shippingInfoService.updateShippingInfo(id, updatedInfo, imageFile);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 根據ID獲取物流詳情
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<ShippingInfo> getShippingInfoById(@PathVariable Long id) {
        return shippingInfoService.getShippingInfoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
