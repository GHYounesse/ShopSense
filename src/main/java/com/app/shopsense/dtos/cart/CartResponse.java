package com.app.shopsense.dtos.cart;

/**
 * Response wrapper for cart operations (optional - for additional metadata)
 */
class CartResponse {


    private CartDto cart;


    private Integer itemCount;


    private String message;

    public CartResponse(CartDto cart, Integer itemCount, String message) {
        this.cart = cart;
        this.itemCount = itemCount;
        this.message = message;
    }

    public CartResponse() {
    }

    public CartDto getCart() {
        return this.cart;
    }

    public Integer getItemCount() {
        return this.itemCount;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CartResponse)) return false;
        final CartResponse other = (CartResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$cart = this.getCart();
        final Object other$cart = other.getCart();
        if (this$cart == null ? other$cart != null : !this$cart.equals(other$cart)) return false;
        final Object this$itemCount = this.getItemCount();
        final Object other$itemCount = other.getItemCount();
        if (this$itemCount == null ? other$itemCount != null : !this$itemCount.equals(other$itemCount)) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CartResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $cart = this.getCart();
        result = result * PRIME + ($cart == null ? 43 : $cart.hashCode());
        final Object $itemCount = this.getItemCount();
        result = result * PRIME + ($itemCount == null ? 43 : $itemCount.hashCode());
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    public String toString() {
        return "CartResponse(cart=" + this.getCart() + ", itemCount=" + this.getItemCount() + ", message=" + this.getMessage() + ")";
    }
}
