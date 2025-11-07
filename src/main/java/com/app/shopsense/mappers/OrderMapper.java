package com.app.shopsense.mappers;
import com.app.shopsense.doas.entities.order.Order;
import com.app.shopsense.doas.entities.order.OrderItem;
import com.app.shopsense.dtos.order.OrderDetailsDto;
import com.app.shopsense.dtos.order.OrderDto;
import com.app.shopsense.dtos.order.OrderItemDto;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "dateCreated",
            source = "dateCreated",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtoList(List<Order> orders);

    @Mapping(source = "orderItems", target = "items")
    OrderDetailsDto toOrderDetailsDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imgUrl", target = "productImageUrl")
    @Mapping(target = "subtotal",
            expression = "java(orderItem.getPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())))")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
