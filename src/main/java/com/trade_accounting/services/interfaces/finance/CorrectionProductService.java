package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.CorrectionProductDto;

import java.util.List;

public interface CorrectionProductService {
    List<CorrectionProductDto> getAll();

    CorrectionProductDto getById(Long id);

    CorrectionProductDto create(CorrectionProductDto correctionProductDto);

    void update(CorrectionProductDto correctionProductDto);

    void deleteById(Long id);
}
