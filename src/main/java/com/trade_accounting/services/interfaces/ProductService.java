package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAll();

    List<ProductDto> getAllLite();

    ProductDto getById(Long id);

    void create(ProductDto productDto);

    void update(ProductDto productDto);

    void deleteById(Long id);

    List<ProductDto> getAllByProductGroup(ProductGroupDto productGroupDto);
}

