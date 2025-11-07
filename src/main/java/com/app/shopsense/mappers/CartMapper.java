package com.app.shopsense.mappers;

import com.app.shopsense.doas.entities.cart.Cart;
import com.app.shopsense.doas.entities.cart.CartItem;
import com.app.shopsense.doas.entities.product.Product;
import com.app.shopsense.dtos.cart.CartDto;
import com.app.shopsense.dtos.cart.CartItemDto;
import com.app.shopsense.dtos.product.ProductDto;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
    @Mapping(target = "product", ignore = true)
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "cartItem.productId", target = "productId")
    @Mapping(source = "cartItem.productName", target = "productName")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(source = "cartItem.price", target = "price")
    @Mapping(source = "cartItem.subTotal", target = "subTotal")
    @Mapping(source = "product", target = "product") // maps Product → ProductDto
    CartItemDto toDto(CartItem cartItem, Product product);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Cart toEntity(CartDto dto);


    ProductDto toProductDto(Product dto);
    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductDto dto);
}
