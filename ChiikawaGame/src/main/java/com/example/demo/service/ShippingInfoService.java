package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Order;
import com.example.demo.model.OrderRepository;
import com.example.demo.model.ShippingInfo;
import com.example.demo.model.ShippingInfoRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ShippingInfoService {

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 文件上傳保存的目錄，請根據實際情況修改
    private static final String UPLOAD_DIR = "/path/to/your/upload/directory";

    // 更新物流信息，包括文件上傳
    public boolean updateShippingInfo(Long shippingInfoId, ShippingInfo updatedInfo, MultipartFile imageFile) {
        return shippingInfoRepository.findById(shippingInfoId).map(info -> {
            info.setShippingInfoRecipient(updatedInfo.getShippingInfoRecipient());
            info.setShippingInfoAddress(updatedInfo.getShippingInfoAddress());
            info.setShippingInfoStatus(updatedInfo.getShippingInfoStatus());

            // 處理文件上傳
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    // 確保上傳目錄存在
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    // 生成唯一的文件名，防止衝突
                    String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                    File destFile = new File(uploadDir, filename);
                    imageFile.transferTo(destFile);

                    // 設置圖片的 URL 或路徑
                    String imageUrl = "/uploads/" + filename; // 根據實際情況設置訪問路徑
                    info.setShippingInfoTrackingImageUrl(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("文件上傳失敗: " + e.getMessage());
                }
            }

            shippingInfoRepository.save(info);
            return true;
        }).orElse(false);
    }

    // 根據ID獲取物流詳情
    public Optional<ShippingInfo> getShippingInfoById(Long shippingInfoId) {
        return shippingInfoRepository.findById(shippingInfoId);
    }

    // 查詢訂單和物流信息的關聯數據
    public List<Map<String, Object>> getOrderShippingInfo(Long orderId, String shippingInfoStatus, Long buyerId) {
        List<Order> orders;

        if (buyerId != null) {
            orders = orderRepository.findByBuyerId(buyerId);
        } else if (orderId != null) {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            orders = orderOptional.map(Collections::singletonList).orElse(Collections.emptyList());
        } else {
            orders = orderRepository.findAll();
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (Order order : orders) {
            List<ShippingInfo> shippingInfos = shippingInfoRepository.findByOrderOrderId(order.getOrderId());
            for (ShippingInfo shippingInfo : shippingInfos) {
                if (shippingInfoStatus == null || shippingInfo.getShippingInfoStatus().equalsIgnoreCase(shippingInfoStatus)) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("order", order);
                    result.put("shippingInfo", shippingInfo);
                    results.add(result);
                }
            }
        }
        return results;
    }
}
