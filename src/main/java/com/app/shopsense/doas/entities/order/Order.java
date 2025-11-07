package com.app.shopsense.doas.entities.order;

import com.app.shopsense.doas.entities.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private String shippingAddress;

    private String billingAddress;

    private String notes;

    private LocalDateTime paymentConfirmedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private String paymentIntentId;

    public Order() {
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public String getBillingAddress() {
        return this.billingAddress;
    }

    public String getNotes() {
        return this.notes;
    }

    public LocalDateTime getPaymentConfirmedAt() {
        return this.paymentConfirmedAt;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public String getPaymentIntentId() {
        return this.paymentIntentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPaymentConfirmedAt(LocalDateTime paymentConfirmedAt) {
        this.paymentConfirmedAt = paymentConfirmedAt;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        final Order other = (Order) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
        final Object this$dateCreated = this.getDateCreated();
        final Object other$dateCreated = other.getDateCreated();
        if (this$dateCreated == null ? other$dateCreated != null : !this$dateCreated.equals(other$dateCreated))
            return false;
        final Object this$total = this.getTotal();
        final Object other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) return false;
        final Object this$orderItems = this.getOrderItems();
        final Object other$orderItems = other.getOrderItems();
        if (this$orderItems == null ? other$orderItems != null : !this$orderItems.equals(other$orderItems))
            return false;
        final Object this$shippingAddress = this.getShippingAddress();
        final Object other$shippingAddress = other.getShippingAddress();
        if (this$shippingAddress == null ? other$shippingAddress != null : !this$shippingAddress.equals(other$shippingAddress))
            return false;
        final Object this$billingAddress = this.getBillingAddress();
        final Object other$billingAddress = other.getBillingAddress();
        if (this$billingAddress == null ? other$billingAddress != null : !this$billingAddress.equals(other$billingAddress))
            return false;
        final Object this$notes = this.getNotes();
        final Object other$notes = other.getNotes();
        if (this$notes == null ? other$notes != null : !this$notes.equals(other$notes)) return false;
        final Object this$paymentConfirmedAt = this.getPaymentConfirmedAt();
        final Object other$paymentConfirmedAt = other.getPaymentConfirmedAt();
        if (this$paymentConfirmedAt == null ? other$paymentConfirmedAt != null : !this$paymentConfirmedAt.equals(other$paymentConfirmedAt))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$paymentIntentId = this.getPaymentIntentId();
        final Object other$paymentIntentId = other.getPaymentIntentId();
        if (this$paymentIntentId == null ? other$paymentIntentId != null : !this$paymentIntentId.equals(other$paymentIntentId))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Order;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        final Object $dateCreated = this.getDateCreated();
        result = result * PRIME + ($dateCreated == null ? 43 : $dateCreated.hashCode());
        final Object $total = this.getTotal();
        result = result * PRIME + ($total == null ? 43 : $total.hashCode());
        final Object $orderItems = this.getOrderItems();
        result = result * PRIME + ($orderItems == null ? 43 : $orderItems.hashCode());
        final Object $shippingAddress = this.getShippingAddress();
        result = result * PRIME + ($shippingAddress == null ? 43 : $shippingAddress.hashCode());
        final Object $billingAddress = this.getBillingAddress();
        result = result * PRIME + ($billingAddress == null ? 43 : $billingAddress.hashCode());
        final Object $notes = this.getNotes();
        result = result * PRIME + ($notes == null ? 43 : $notes.hashCode());
        final Object $paymentConfirmedAt = this.getPaymentConfirmedAt();
        result = result * PRIME + ($paymentConfirmedAt == null ? 43 : $paymentConfirmedAt.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $paymentIntentId = this.getPaymentIntentId();
        result = result * PRIME + ($paymentIntentId == null ? 43 : $paymentIntentId.hashCode());
        return result;
    }

    public String toString() {
        return "Order(id=" + this.getId() + ", user=" + this.getUser() + ", dateCreated=" + this.getDateCreated() + ", total=" + this.getTotal() + ", orderItems=" + this.getOrderItems() + ", shippingAddress=" + this.getShippingAddress() + ", billingAddress=" + this.getBillingAddress() + ", notes=" + this.getNotes() + ", paymentConfirmedAt=" + this.getPaymentConfirmedAt() + ", status=" + this.getStatus() + ", paymentIntentId=" + this.getPaymentIntentId() + ")";
    }
}
