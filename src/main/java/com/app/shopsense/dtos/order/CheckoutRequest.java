package com.app.shopsense.dtos.order;

import jakarta.validation.constraints.Pattern;

import java.util.Map;

public class CheckoutRequest {

    @Pattern(regexp = "^[a-z]{3}$", message = "Currency must be 3 lowercase letters (e.g., usd, eur)")
    private String currency = "usd";

    private String shippingAddress;

    private String billingAddress;

    private String notes;

    // Metadata to attach to the payment intent
    private Map<String, String> metadata;

    public CheckoutRequest() {
    }

    public @Pattern(regexp = "^[a-z]{3}$", message = "Currency must be 3 lowercase letters (e.g., usd, eur)") String getCurrency() {
        return this.currency;
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

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public void setCurrency(@Pattern(regexp = "^[a-z]{3}$", message = "Currency must be 3 lowercase letters (e.g., usd, eur)") String currency) {
        this.currency = currency;
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

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CheckoutRequest)) return false;
        final CheckoutRequest other = (CheckoutRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
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
        final Object this$metadata = this.getMetadata();
        final Object other$metadata = other.getMetadata();
        if (this$metadata == null ? other$metadata != null : !this$metadata.equals(other$metadata)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CheckoutRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $shippingAddress = this.getShippingAddress();
        result = result * PRIME + ($shippingAddress == null ? 43 : $shippingAddress.hashCode());
        final Object $billingAddress = this.getBillingAddress();
        result = result * PRIME + ($billingAddress == null ? 43 : $billingAddress.hashCode());
        final Object $notes = this.getNotes();
        result = result * PRIME + ($notes == null ? 43 : $notes.hashCode());
        final Object $metadata = this.getMetadata();
        result = result * PRIME + ($metadata == null ? 43 : $metadata.hashCode());
        return result;
    }

    public String toString() {
        return "CheckoutRequest(currency=" + this.getCurrency() + ", shippingAddress=" + this.getShippingAddress() + ", billingAddress=" + this.getBillingAddress() + ", notes=" + this.getNotes() + ", metadata=" + this.getMetadata() + ")";
    }
}
