package com.app.shopsense.dtos.payment;

import java.math.BigDecimal;

public class PaymentIntentDto {

    private String clientSecret;

    private String paymentIntentId;

    private BigDecimal amount;

    private String currency;

    private String status;

    PaymentIntentDto(String clientSecret, String paymentIntentId, BigDecimal amount, String currency, String status) {
        this.clientSecret = clientSecret;
        this.paymentIntentId = paymentIntentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    public static PaymentIntentDtoBuilder builder() {
        return new PaymentIntentDtoBuilder();
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getPaymentIntentId() {
        return this.paymentIntentId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getStatus() {
        return this.status;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PaymentIntentDto)) return false;
        final PaymentIntentDto other = (PaymentIntentDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$clientSecret = this.getClientSecret();
        final Object other$clientSecret = other.getClientSecret();
        if (this$clientSecret == null ? other$clientSecret != null : !this$clientSecret.equals(other$clientSecret))
            return false;
        final Object this$paymentIntentId = this.getPaymentIntentId();
        final Object other$paymentIntentId = other.getPaymentIntentId();
        if (this$paymentIntentId == null ? other$paymentIntentId != null : !this$paymentIntentId.equals(other$paymentIntentId))
            return false;
        final Object this$amount = this.getAmount();
        final Object other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !this$amount.equals(other$amount)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PaymentIntentDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $clientSecret = this.getClientSecret();
        result = result * PRIME + ($clientSecret == null ? 43 : $clientSecret.hashCode());
        final Object $paymentIntentId = this.getPaymentIntentId();
        result = result * PRIME + ($paymentIntentId == null ? 43 : $paymentIntentId.hashCode());
        final Object $amount = this.getAmount();
        result = result * PRIME + ($amount == null ? 43 : $amount.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "PaymentIntentDto(clientSecret=" + this.getClientSecret() + ", paymentIntentId=" + this.getPaymentIntentId() + ", amount=" + this.getAmount() + ", currency=" + this.getCurrency() + ", status=" + this.getStatus() + ")";
    }

    public static class PaymentIntentDtoBuilder {
        private String clientSecret;
        private String paymentIntentId;
        private BigDecimal amount;
        private String currency;
        private String status;

        PaymentIntentDtoBuilder() {
        }

        public PaymentIntentDtoBuilder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public PaymentIntentDtoBuilder paymentIntentId(String paymentIntentId) {
            this.paymentIntentId = paymentIntentId;
            return this;
        }

        public PaymentIntentDtoBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentIntentDtoBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentIntentDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PaymentIntentDto build() {
            return new PaymentIntentDto(this.clientSecret, this.paymentIntentId, this.amount, this.currency, this.status);
        }

        public String toString() {
            return "PaymentIntentDto.PaymentIntentDtoBuilder(clientSecret=" + this.clientSecret + ", paymentIntentId=" + this.paymentIntentId + ", amount=" + this.amount + ", currency=" + this.currency + ", status=" + this.status + ")";
        }
    }
}
