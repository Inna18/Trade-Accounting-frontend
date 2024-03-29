package com.trade_accounting.services.interfaces.production;

import com.trade_accounting.models.dto.production.TechnicalCardDto;

import java.util.List;
import java.util.Map;

public interface TechnicalCardService {

    List<TechnicalCardDto> searchTechnicalCard(Map<String, String> queryTechnicalCard);

    List<TechnicalCardDto> getAll();

    TechnicalCardDto getById(Long id);

    void create(TechnicalCardDto dto);

    void update(TechnicalCardDto dto);

    void deleteById(Long id);

    List<TechnicalCardDto> search(String searchTerm);

    List<TechnicalCardDto> getAllByTechnicalCardGroupId(Long id);
}
