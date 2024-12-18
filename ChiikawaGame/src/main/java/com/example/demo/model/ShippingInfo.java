package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "shipping_info")
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_info_id")
    private Long shippingInfoId; // 物流信息 ID

    @Column(name = "shipping_info_recipient", nullable = false)
    @NotNull
    @Size(max = 255)
    private String shippingInfoRecipient; // 收件人

    @Column(name = "shipping_info_address", nullable = false)
    @NotNull
    @Size(max = 500)
    private String shippingInfoAddress; // 地址

    @Column(name = "shipping_info_status", nullable = false)
    @NotNull
    private String shippingInfoStatus; // 物流狀態

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    @NotNull
    private Order order; // 關聯的訂單

    @Column(name = "shipping_info_tracking_number")
    @Size(max = 100)
    private String shippingInfoTrackingNumber; // 物流追蹤號

    @Column(name = "shipping_info_tracking_image_url")
    @Size(max = 500)
    private String shippingInfoTrackingImageUrl; // 物流單號圖片的 URL

    // Constructors
    public ShippingInfo() {
    }

    // Getters and Setters

    public Long getShippingInfoId() {
        return shippingInfoId;
    }

    public void setShippingInfoId(Long shippingInfoId) {
        this.shippingInfoId = shippingInfoId;
    }

    public String getShippingInfoRecipient() {
        return shippingInfoRecipient;
    }

    public void setShippingInfoRecipient(String shippingInfoRecipient) {
        this.shippingInfoRecipient = shippingInfoRecipient;
    }

    public String getShippingInfoAddress() {
        return shippingInfoAddress;
    }

    public void setShippingInfoAddress(String shippingInfoAddress) {
        this.shippingInfoAddress = shippingInfoAddress;
    }

    public String getShippingInfoStatus() {
        return shippingInfoStatus;
    }

    public void setShippingInfoStatus(String shippingInfoStatus) {
        this.shippingInfoStatus = shippingInfoStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null && !order.getShippingInfos().contains(this)) {
            order.getShippingInfos().add(this);
        }
    }

    public String getShippingInfoTrackingNumber() {
        return shippingInfoTrackingNumber;
    }

    public void setShippingInfoTrackingNumber(String shippingInfoTrackingNumber) {
        this.shippingInfoTrackingNumber = shippingInfoTrackingNumber;
    }

    public String getShippingInfoTrackingImageUrl() {
        return shippingInfoTrackingImageUrl;
    }

    public void setShippingInfoTrackingImageUrl(String shippingInfoTrackingImageUrl) {
        this.shippingInfoTrackingImageUrl = shippingInfoTrackingImageUrl;
    }

    // equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShippingInfo that = (ShippingInfo) o;

        return Objects.equals(shippingInfoId, that.shippingInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shippingInfoId);
    }
}
