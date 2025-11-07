package com.app.shopsense.dtos.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsDto {

    private Long id;

    private BigDecimal total;

    private String status;

    private LocalDateTime dateCreated;

    private List<OrderItemDto> items;

    private String shippingAddress;

    private String paymentIntentId;

    public OrderDetailsDto() {
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public List<OrderItemDto> getItems() {
        return this.items;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public String getPaymentIntentId() {
        return this.paymentIntentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OrderDetailsDto)) return false;
        final OrderDetailsDto other = (OrderDetailsDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$total = this.getTotal();
        final Object other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$dateCreated = this.getDateCreated();
        final Object other$dateCreated = other.getDateCreated();
        if (this$dateCreated == null ? other$dateCreated != null : !this$dateCreated.equals(other$dateCreated))
            return false;
        final Object this$items = this.getItems();
        final Object other$items = other.getItems();
        if (this$items == null ? other$items != null : !this$items.equals(other$items)) return false;
        final Object this$shippingAddress = this.getShippingAddress();
        final Object other$shippingAddress = other.getShippingAddress();
        if (this$shippingAddress == null ? other$shippingAddress != null : !this$shippingAddress.equals(other$shippingAddress))
            return false;
        final Object this$paymentIntentId = this.getPaymentIntentId();
        final Object other$paymentIntentId = other.getPaymentIntentId();
        if (this$paymentIntentId == null ? other$paymentIntentId != null : !this$paymentIntentId.equals(other$paymentIntentId))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OrderDetailsDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $total = this.getTotal();
        result = result * PRIME + ($total == null ? 43 : $total.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $dateCreated = this.getDateCreated();
        result = result * PRIME + ($dateCreated == null ? 43 : $dateCreated.hashCode());
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        final Object $shippingAddress = this.getShippingAddress();
        result = result * PRIME + ($shippingAddress == null ? 43 : $shippingAddress.hashCode());
        final Object $paymentIntentId = this.getPaymentIntentId();
        result = result * PRIME + ($paymentIntentId == null ? 43 : $paymentIntentId.hashCode());
        return result;
    }

    public String toString() {
        return "OrderDetailsDto(id=" + this.getId() + ", total=" + this.getTotal() + ", status=" + this.getStatus() + ", dateCreated=" + this.getDateCreated() + ", items=" + this.getItems() + ", shippingAddress=" + this.getShippingAddress() + ", paymentIntentId=" + this.getPaymentIntentId() + ")";
    }
}
