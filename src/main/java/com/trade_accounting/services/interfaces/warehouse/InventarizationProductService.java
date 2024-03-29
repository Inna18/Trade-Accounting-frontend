package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.InventarizationProductDto;

import java.util.List;

public interface InventarizationProductService {

    List<InventarizationProductDto> getAll();

    InventarizationProductDto getById(Long id);

    InventarizationProductDto create(InventarizationProductDto dto);

    void update(InventarizationProductDto dto);

    void deleteById(Long id);
}
