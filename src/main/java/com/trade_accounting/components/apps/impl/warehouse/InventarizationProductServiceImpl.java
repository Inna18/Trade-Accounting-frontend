package com.trade_accounting.components.apps.impl.warehouse;


import com.trade_accounting.models.dto.warehouse.InventarizationProductDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.warehouse.InventarizationProductService;
import com.trade_accounting.services.api.warehouse.InventarizationProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class InventarizationProductServiceImpl implements InventarizationProductService {

    private final InventarizationProductApi inventarizationProductApi;
    private final String inventarizationProductUrl;
    private final CallExecuteService<InventarizationProductDto> callExecuteService;

    public InventarizationProductServiceImpl(Retrofit retrofit, @Value("${inventarization_product_url}") String inventarizationProductUrl, CallExecuteService<InventarizationProductDto> callExecuteService) {
        inventarizationProductApi = retrofit.create(InventarizationProductApi.class);
        this.inventarizationProductUrl = inventarizationProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<InventarizationProductDto> getAll() {
        return null;
    }

    @Override
    public InventarizationProductDto getById(Long id) {
        Call<InventarizationProductDto> inventarizationProductDtoCall = inventarizationProductApi.getById(inventarizationProductUrl, id);
        return callExecuteService.callExecuteBodyById(inventarizationProductDtoCall, InventarizationProductDto.class, id);
    }

    @Override
    public InventarizationProductDto create(InventarizationProductDto inventarizationProductDto) {
        return null;
    }

    @Override
    public void update(InventarizationProductDto inventarizationProductDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
