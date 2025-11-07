package com.app.shopsense.dtos.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductDto {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    @NotBlank(message = "Image URL is required")
    private String imgUrl;

    public ProductDto() {
    }


    public @NotBlank(message = "Product name is required") String getName() {
        return this.name;
    }

    public @NotBlank(message = "Description is required") String getDescription() {
        return this.description;
    }

    public @NotNull(message = "Price is required") @Positive(message = "Price must be positive") BigDecimal getPrice() {
        return this.price;
    }

    public @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer getQuantity() {
        return this.quantity;
    }

    public @NotBlank(message = "Image URL is required") String getImgUrl() {
        return this.imgUrl;
    }

    public void setName(@NotBlank(message = "Product name is required") String name) {
        this.name = name;
    }

    public void setDescription(@NotBlank(message = "Description is required") String description) {
        this.description = description;
    }

    public void setPrice(@NotNull(message = "Price is required") @Positive(message = "Price must be positive") BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(@NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @Max(value = 999, message = "Quantity cannot exceed 999") Integer quantity) {
        this.quantity = quantity;
    }

    public void setImgUrl(@NotBlank(message = "Image URL is required") String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProductDto)) return false;
        final ProductDto other = (ProductDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        final Object this$imgUrl = this.getImgUrl();
        final Object other$imgUrl = other.getImgUrl();
        if (this$imgUrl == null ? other$imgUrl != null : !this$imgUrl.equals(other$imgUrl)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProductDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $imgUrl = this.getImgUrl();
        result = result * PRIME + ($imgUrl == null ? 43 : $imgUrl.hashCode());
        return result;
    }

    public String toString() {
        return "ProductDto(name=" + this.getName() + ", description=" + this.getDescription() + ", price=" + this.getPrice() + ", quantity=" + this.getQuantity() + ", imgUrl=" + this.getImgUrl() + ")";
    }
}
