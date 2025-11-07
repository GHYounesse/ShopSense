package com.app.shopsense.dtos.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding items to cart
 */

public class AddToCartRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    public AddToCartRequest(@NotNull(message = "Product ID is required") Long productId, @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public AddToCartRequest() {
    }

    public @NotNull(message = "Product ID is required") Long getProductId() {
        return this.productId;
    }

    public @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer getQuantity() {
        return this.quantity;
    }

    public void setProductId(@NotNull(message = "Product ID is required") Long productId) {
        this.productId = productId;
    }

    public void setQuantity(@NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer quantity) {
        this.quantity = quantity;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AddToCartRequest)) return false;
        final AddToCartRequest other = (AddToCartRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$productId = this.getProductId();
        final Object other$productId = other.getProductId();
        if (this$productId == null ? other$productId != null : !this$productId.equals(other$productId)) return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AddToCartRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $productId = this.getProductId();
        result = result * PRIME + ($productId == null ? 43 : $productId.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        return result;
    }

    public String toString() {
        return "AddToCartRequest(productId=" + this.getProductId() + ", quantity=" + this.getQuantity() + ")";
    }
}
