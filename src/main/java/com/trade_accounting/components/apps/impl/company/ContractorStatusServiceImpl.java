package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.ContractorStatusDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.api.company.ContractorStatusApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class ContractorStatusServiceImpl implements ContractorStatusService {

    private final ContractorStatusApi contractorStatusApi;

    private final String statusUrl;

    private final CallExecuteService<ContractorStatusDto> dtoCallExecuteService;

    public ContractorStatusServiceImpl(@Value("${contractor_status_url}") String statusUrl, Retrofit retrofit, CallExecuteService<ContractorStatusDto> dtoCallExecuteService) {
        this.contractorStatusApi = retrofit.create(ContractorStatusApi.class);
        this.statusUrl = statusUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ContractorStatusDto> getAll() {
        Call<List<ContractorStatusDto>> statusGetAllCall = contractorStatusApi.getAll(statusUrl);
        return dtoCallExecuteService.callExecuteBodyList(statusGetAllCall, ContractorStatusDto.class);
    }

    @Override
    public ContractorStatusDto getById(Long id) {
        Call<ContractorStatusDto> statusGetCall = contractorStatusApi.getById(statusUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(statusGetCall, ContractorStatusDto.class, id);
    }

}
