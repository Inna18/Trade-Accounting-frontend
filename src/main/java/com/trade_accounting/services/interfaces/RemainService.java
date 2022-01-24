package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.RemainDto;

import java.util.List;

public interface RemainService {

    List<RemainDto> getAll();

    RemainDto getById(Long id);

    RemainDto create(RemainDto remainDto);

    void update(RemainDto remainDto);

    void deleteById(Long id);

    List<RemainDto> getAll(String typeOfRemain);

    List<RemainDto> findBySearchAndTypeOfRemain(String search, String typeOfRemain);

}
