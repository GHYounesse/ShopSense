package com.app.shopsense.dtos.order;

import java.math.BigDecimal;

public class OrderDto {
    private Long id;
    private String dateCreated;
    private BigDecimal total;

    public OrderDto() {
    }

    public Long getId() {
        return this.id;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OrderDto)) return false;
        final OrderDto other = (OrderDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$dateCreated = this.getDateCreated();
        final Object other$dateCreated = other.getDateCreated();
        if (this$dateCreated == null ? other$dateCreated != null : !this$dateCreated.equals(other$dateCreated))
            return false;
        final Object this$total = this.getTotal();
        final Object other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OrderDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $dateCreated = this.getDateCreated();
        result = result * PRIME + ($dateCreated == null ? 43 : $dateCreated.hashCode());
        final Object $total = this.getTotal();
        result = result * PRIME + ($total == null ? 43 : $total.hashCode());
        return result;
    }

    public String toString() {
        return "OrderDto(id=" + this.getId() + ", dateCreated=" + this.getDateCreated() + ", total=" + this.getTotal() + ")";
    }
}
