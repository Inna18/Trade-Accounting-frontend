package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.TypeOfPriceDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.api.company.TypeOfPriceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TypeOfPriceServiceImpl implements TypeOfPriceService {

    private final TypeOfPriceApi typeOfPriceApi;

    private final String typeOfPriceUrl;

    private final CallExecuteService<TypeOfPriceDto> dtoCallExecuteService;

    public TypeOfPriceServiceImpl(@Value("${type_of_price_url}") String typeOfPriceUrl, Retrofit retrofit, CallExecuteService<TypeOfPriceDto> dtoCallExecuteService) {
        this.typeOfPriceUrl = typeOfPriceUrl;
        typeOfPriceApi = retrofit.create(TypeOfPriceApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TypeOfPriceDto> getAll() {
        Call<List<TypeOfPriceDto>> typeOfPriceDtoListCall = typeOfPriceApi.getAll(typeOfPriceUrl);
        return dtoCallExecuteService.callExecuteBodyList(typeOfPriceDtoListCall, TypeOfPriceDto.class);
    }

    @Override
    public TypeOfPriceDto getById(Long id) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.getById(typeOfPriceUrl, id);
       return dtoCallExecuteService.callExecuteBodyById(typeOfPriceDtoCall, TypeOfPriceDto.class, id);
    }

    @Override
    public void create(TypeOfPriceDto typeOfPriceDto) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.create(typeOfPriceUrl, typeOfPriceDto);
        dtoCallExecuteService.callExecuteBodyCreate(typeOfPriceDtoCall, TypeOfPriceDto.class);
    }

    @Override
    public void update(TypeOfPriceDto typeOfPriceDto) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.update(typeOfPriceUrl, typeOfPriceDto);
        dtoCallExecuteService.callExecuteBodyUpdate(typeOfPriceDtoCall, TypeOfPriceDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.deleteById(typeOfPriceUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(typeOfPriceDtoCall, TypeOfPriceDto.class, id);
    }
}
