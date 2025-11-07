package com.app.shopsense.mappers;
import com.app.shopsense.doas.entities.product.Product;
import com.app.shopsense.dtos.product.ProductDto;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductDto productDto);

    ProductDto toProductDto(Product product);

    List<ProductDto> toProductDtoList(List<Product> products);
}
