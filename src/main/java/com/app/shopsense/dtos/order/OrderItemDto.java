package com.app.shopsense.dtos.order;

import java.math.BigDecimal;

public class OrderItemDto {

    private Long productId;

    private String productName;

    private String productImageUrl;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;

    public OrderItemDto() {
    }

    public Long getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getProductImageUrl() {
        return this.productImageUrl;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OrderItemDto)) return false;
        final OrderItemDto other = (OrderItemDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$productId = this.getProductId();
        final Object other$productId = other.getProductId();
        if (this$productId == null ? other$productId != null : !this$productId.equals(other$productId)) return false;
        final Object this$productName = this.getProductName();
        final Object other$productName = other.getProductName();
        if (this$productName == null ? other$productName != null : !this$productName.equals(other$productName))
            return false;
        final Object this$productImageUrl = this.getProductImageUrl();
        final Object other$productImageUrl = other.getProductImageUrl();
        if (this$productImageUrl == null ? other$productImageUrl != null : !this$productImageUrl.equals(other$productImageUrl))
            return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$subtotal = this.getSubtotal();
        final Object other$subtotal = other.getSubtotal();
        if (this$subtotal == null ? other$subtotal != null : !this$subtotal.equals(other$subtotal)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OrderItemDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $productId = this.getProductId();
        result = result * PRIME + ($productId == null ? 43 : $productId.hashCode());
        final Object $productName = this.getProductName();
        result = result * PRIME + ($productName == null ? 43 : $productName.hashCode());
        final Object $productImageUrl = this.getProductImageUrl();
        result = result * PRIME + ($productImageUrl == null ? 43 : $productImageUrl.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $subtotal = this.getSubtotal();
        result = result * PRIME + ($subtotal == null ? 43 : $subtotal.hashCode());
        return result;
    }

    public String toString() {
        return "OrderItemDto(productId=" + this.getProductId() + ", productName=" + this.getProductName() + ", productImageUrl=" + this.getProductImageUrl() + ", quantity=" + this.getQuantity() + ", price=" + this.getPrice() + ", subtotal=" + this.getSubtotal() + ")";
    }
}
