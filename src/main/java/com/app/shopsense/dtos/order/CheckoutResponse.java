package com.app.shopsense.dtos.order;

import java.math.BigDecimal;

public class CheckoutResponse {

    private String clientSecret;

    private Long orderId;

    private BigDecimal totalAmount;

    private String currency;

    private String paymentIntentId;

    private String status;

    public CheckoutResponse(String clientSecret, Long orderId, BigDecimal totalAmount, String currency, String paymentIntentId, String status) {
        this.clientSecret = clientSecret;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.paymentIntentId = paymentIntentId;
        this.status = status;
    }

    public CheckoutResponse() {
    }

    public static CheckoutResponseBuilder builder() {
        return new CheckoutResponseBuilder();
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getPaymentIntentId() {
        return this.paymentIntentId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CheckoutResponse)) return false;
        final CheckoutResponse other = (CheckoutResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$clientSecret = this.getClientSecret();
        final Object other$clientSecret = other.getClientSecret();
        if (this$clientSecret == null ? other$clientSecret != null : !this$clientSecret.equals(other$clientSecret))
            return false;
        final Object this$orderId = this.getOrderId();
        final Object other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) return false;
        final Object this$totalAmount = this.getTotalAmount();
        final Object other$totalAmount = other.getTotalAmount();
        if (this$totalAmount == null ? other$totalAmount != null : !this$totalAmount.equals(other$totalAmount))
            return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$paymentIntentId = this.getPaymentIntentId();
        final Object other$paymentIntentId = other.getPaymentIntentId();
        if (this$paymentIntentId == null ? other$paymentIntentId != null : !this$paymentIntentId.equals(other$paymentIntentId))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CheckoutResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $clientSecret = this.getClientSecret();
        result = result * PRIME + ($clientSecret == null ? 43 : $clientSecret.hashCode());
        final Object $orderId = this.getOrderId();
        result = result * PRIME + ($orderId == null ? 43 : $orderId.hashCode());
        final Object $totalAmount = this.getTotalAmount();
        result = result * PRIME + ($totalAmount == null ? 43 : $totalAmount.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $paymentIntentId = this.getPaymentIntentId();
        result = result * PRIME + ($paymentIntentId == null ? 43 : $paymentIntentId.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "CheckoutResponse(clientSecret=" + this.getClientSecret() + ", orderId=" + this.getOrderId() + ", totalAmount=" + this.getTotalAmount() + ", currency=" + this.getCurrency() + ", paymentIntentId=" + this.getPaymentIntentId() + ", status=" + this.getStatus() + ")";
    }

    public static class CheckoutResponseBuilder {
        private String clientSecret;
        private Long orderId;
        private BigDecimal totalAmount;
        private String currency;
        private String paymentIntentId;
        private String status;

        CheckoutResponseBuilder() {
        }

        public CheckoutResponseBuilder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public CheckoutResponseBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public CheckoutResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public CheckoutResponseBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public CheckoutResponseBuilder paymentIntentId(String paymentIntentId) {
            this.paymentIntentId = paymentIntentId;
            return this;
        }

        public CheckoutResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public CheckoutResponse build() {
            return new CheckoutResponse(this.clientSecret, this.orderId, this.totalAmount, this.currency, this.paymentIntentId, this.status);
        }

        public String toString() {
            return "CheckoutResponse.CheckoutResponseBuilder(clientSecret=" + this.clientSecret + ", orderId=" + this.orderId + ", totalAmount=" + this.totalAmount + ", currency=" + this.currency + ", paymentIntentId=" + this.paymentIntentId + ", status=" + this.status + ")";
        }
    }
}
