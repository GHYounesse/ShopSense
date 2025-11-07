package com.app.shopsense.dtos.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateCartItemRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    public UpdateCartItemRequest(@NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer quantity) {
        this.quantity = quantity;
    }

    public UpdateCartItemRequest() {
    }

    public @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(@NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer quantity) {
        this.quantity = quantity;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UpdateCartItemRequest)) return false;
        final UpdateCartItemRequest other = (UpdateCartItemRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UpdateCartItemRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        return result;
    }

    public String toString() {
        return "UpdateCartItemRequest(quantity=" + this.getQuantity() + ")";
    }
}
