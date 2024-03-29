package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.TypeOfContractorDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.company.TypeOfContractorService;
import com.trade_accounting.services.api.company.TypeOfContractorApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TypeOfContractorServiceImpl implements TypeOfContractorService {

    private final TypeOfContractorApi typeOfContractorApi;

    private final String typeOfContractorUrl;

    private final CallExecuteService<TypeOfContractorDto> dtoCallExecuteService;

    public TypeOfContractorServiceImpl(@Value("${type_of_contractor_url}") String typeOfContractorUrl, Retrofit retrofit, CallExecuteService<TypeOfContractorDto> dtoCallExecuteService) {
        this.typeOfContractorUrl = typeOfContractorUrl;
        typeOfContractorApi = retrofit.create(TypeOfContractorApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TypeOfContractorDto> getAll() {
        Call<List<TypeOfContractorDto>> typeOfContractorDtoListCall = typeOfContractorApi.getAll(typeOfContractorUrl);
        return dtoCallExecuteService.callExecuteBodyList(typeOfContractorDtoListCall, TypeOfContractorDto.class);
    }

    @Override
    public TypeOfContractorDto getById(Long id) {
        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.getById(typeOfContractorUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(typeOfContractorDtoCall, TypeOfContractorDto.class, id);
    }

    @Override
    public void create(TypeOfContractorDto typeOfContractorDto) {
        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.create(typeOfContractorUrl, typeOfContractorDto);
        dtoCallExecuteService.callExecuteBodyCreate(typeOfContractorDtoCall, TypeOfContractorDto.class);
    }

    @Override
    public void update(TypeOfContractorDto typeOfContractorDto) {
        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.update(typeOfContractorUrl, typeOfContractorDto);
        dtoCallExecuteService.callExecuteBodyUpdate(typeOfContractorDtoCall, TypeOfContractorDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> typeOfContractorDtoCall = typeOfContractorApi.deleteById(typeOfContractorUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(typeOfContractorDtoCall, TypeOfContractorDto.class, id);
    }
}