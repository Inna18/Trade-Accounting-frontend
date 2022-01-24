package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.CorrectionDto;

import java.util.List;

public interface CorrectionService {

    List<CorrectionDto> getAll();

    CorrectionDto getById(Long id);

    CorrectionDto create(CorrectionDto correctionDto);

    void update(CorrectionDto correctionDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}
