package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.TaxSystemDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.company.TaxSystemService;
import com.trade_accounting.services.api.company.TaxSystemApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TaxSystemServiceImpl implements TaxSystemService {

    private final TaxSystemApi taxSystemApi;

    private final String taxSystemUrl;

    private final CallExecuteService<TaxSystemDto> dtoCallExecuteService;

    public TaxSystemServiceImpl(@Value("${tax_system_url}") String taxSystemUrl, Retrofit retrofit, CallExecuteService<TaxSystemDto> dtoCallExecuteService) {
        taxSystemApi = retrofit.create(TaxSystemApi.class);
        this.taxSystemUrl = taxSystemUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TaxSystemDto> getAll() {
        Call<List<TaxSystemDto>> taxSystemDtoListCall = taxSystemApi.getAll(taxSystemUrl);
        return dtoCallExecuteService.callExecuteBodyList(taxSystemDtoListCall, TaxSystemDto.class);
    }

    @Override
    public TaxSystemDto getById(Long id) {
        Call<TaxSystemDto> taxSystemDtoCall = taxSystemApi.getById(taxSystemUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(taxSystemDtoCall, TaxSystemDto.class, id);
    }

    @Override
    public void create(TaxSystemDto taxSystemDto) {
        Call<Void> taxSystemDtoCall = taxSystemApi.create(taxSystemUrl, taxSystemDto);
        dtoCallExecuteService.callExecuteBodyCreate(taxSystemDtoCall, TaxSystemDto.class);
    }

    @Override
    public void update(TaxSystemDto taxSystemDto) {
        Call<Void> taxSystemDtoCall = taxSystemApi.update(taxSystemUrl, taxSystemDto);
        dtoCallExecuteService.callExecuteBodyUpdate(taxSystemDtoCall, TaxSystemDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taxSystemDtoCall = taxSystemApi.deleteById(taxSystemUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(taxSystemDtoCall, TaxSystemDto.class, id);
    }
}
