package com.trade_accounting.components.apps.impl.retail;

import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.trade_accounting.services.api.retail.RetailStoreApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RetailStoreServiceImpl implements RetailStoreService {

    private final RetailStoreApi retailStoreApi;

    private final String retailStoreUrl;

    private List<RetailStoreDto> retailStoreDtoList = new ArrayList<>();

    private final CallExecuteService<RetailStoreDto> dtoCallExecuteService;

    public RetailStoreServiceImpl(Retrofit retrofit, @Value("${retail_stores_url}") String retailStoreUrl,
                                  CallExecuteService<RetailStoreDto> dtoCallExecuteService) {
        retailStoreApi = retrofit.create(RetailStoreApi.class);
        this.retailStoreUrl = retailStoreUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailStoreDto> getAll() {
        Call<List<RetailStoreDto>> retailStoreDtoListCall = retailStoreApi.getAll(retailStoreUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailStoreDtoListCall, RetailStoreDto.class);
    }

    @Override
    public RetailStoreDto getById(Long id) {
        Call<RetailStoreDto> retailStoreDtoCall = retailStoreApi.getById(retailStoreUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailStoreDtoCall, RetailStoreDto.class, id);
    }

    @Override
    public void create(RetailStoreDto retailStoreDto) {
        Call<Void> retailStoreDtoCall = retailStoreApi.create(retailStoreUrl, retailStoreDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailStoreDtoCall, RetailStoreDto.class);
    }

    @Override
    public void update(RetailStoreDto retailStoreDto) {
        Call<Void> retailStoreDtoCall = retailStoreApi.update(retailStoreUrl, retailStoreDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailStoreDtoCall, RetailStoreDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailStoreDtoCall = retailStoreApi.deleteById(retailStoreUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailStoreDtoCall, RetailStoreDto.class, id);
    }

    @Override
    public List<RetailStoreDto> search(String query) {
        Call<List<RetailStoreDto>> retailStoreDtoListCall = retailStoreApi.search(retailStoreUrl, query);
        try {
            retailStoreDtoList = retailStoreDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка RetailStoreDto по быстрому поиску");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RetailStoreDto по быстрому поиску - ", e);
        }
        return retailStoreDtoList;
    }

    @Override
    public List<RetailStoreDto> searchRetailStoreByFilter(Map<String, String> queryRetailReturns) {
        List<RetailStoreDto> retailRetailStoreDtoList = new ArrayList<>();
        Call<List<RetailStoreDto>> retailStoreListCall = retailStoreApi.searchRetailStoreByFilter(retailStoreUrl, queryRetailReturns);
        try {
            retailRetailStoreDtoList = retailStoreListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка RetailStoreDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RetailStoreDto - ", e);
        }
        return retailRetailStoreDtoList;
    }
}
