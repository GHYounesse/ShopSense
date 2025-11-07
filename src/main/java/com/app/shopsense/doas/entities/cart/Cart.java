package com.app.shopsense.doas.entities.cart;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(Long id, Long userId, BigDecimal totalPrice, List<CartItem> cartItems) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public Cart() {
    }

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Cart)) return false;
        final Cart other = (Cart) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        final Object this$totalPrice = this.getTotalPrice();
        final Object other$totalPrice = other.getTotalPrice();
        if (this$totalPrice == null ? other$totalPrice != null : !this$totalPrice.equals(other$totalPrice))
            return false;
        final Object this$cartItems = this.getCartItems();
        final Object other$cartItems = other.getCartItems();
        if (this$cartItems == null ? other$cartItems != null : !this$cartItems.equals(other$cartItems)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Cart;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $totalPrice = this.getTotalPrice();
        result = result * PRIME + ($totalPrice == null ? 43 : $totalPrice.hashCode());
        final Object $cartItems = this.getCartItems();
        result = result * PRIME + ($cartItems == null ? 43 : $cartItems.hashCode());
        return result;
    }

    public String toString() {
        return "Cart(id=" + this.getId() + ", userId=" + this.getUserId() + ", totalPrice=" + this.getTotalPrice() + ", cartItems=" + this.getCartItems() + ")";
    }
}
