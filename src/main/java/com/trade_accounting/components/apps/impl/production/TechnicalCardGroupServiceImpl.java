package com.trade_accounting.components.apps.impl.production;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.production.TechnicalCardGroupDto;
import com.trade_accounting.services.interfaces.production.TechnicalCardGroupService;
import com.trade_accounting.services.api.production.TechnicalCardGroupApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TechnicalCardGroupServiceImpl implements TechnicalCardGroupService {

    private final String technicalCardGroupUrl;

    private final TechnicalCardGroupApi technicalCardGroupApi;

    private final CallExecuteService<TechnicalCardGroupDto> dtoCallExecuteService;

    public TechnicalCardGroupServiceImpl(@Value("${technical_card_group_url}") String technicalCardGroupUrl, Retrofit retrofit, CallExecuteService<TechnicalCardGroupDto> dtoCallExecuteService) {
        this.technicalCardGroupUrl = technicalCardGroupUrl;
        technicalCardGroupApi = retrofit.create(TechnicalCardGroupApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalCardGroupDto> getAll() {
        Call<List<TechnicalCardGroupDto>> technicalCardGroupGetAll = technicalCardGroupApi.getAll(technicalCardGroupUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalCardGroupGetAll, TechnicalCardGroupDto.class);
    }

    @Override
    public TechnicalCardGroupDto getById(Long id) {
        Call<TechnicalCardGroupDto> technicalCardGroupDtoGetCall = technicalCardGroupApi.getById(technicalCardGroupUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalCardGroupDtoGetCall, TechnicalCardGroupDto.class, id);

    }

    @Override
    public void create(TechnicalCardGroupDto technicalCardGroupDto) {
        Call<Void> technicalCardGroupCreateCall = technicalCardGroupApi.create(technicalCardGroupUrl, technicalCardGroupDto);
        dtoCallExecuteService.callExecuteBodyCreate(technicalCardGroupCreateCall, TechnicalCardGroupDto.class);
    }

    @Override
    public void update(TechnicalCardGroupDto technicalCardGroupDto) {
        Call<Void> technicalCardGroupUpdateCall = technicalCardGroupApi.update(technicalCardGroupUrl, technicalCardGroupDto);
        dtoCallExecuteService.callExecuteBodyUpdate(technicalCardGroupUpdateCall, TechnicalCardGroupDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> technicalCardGroupDeleteCall = technicalCardGroupApi.deleteById(technicalCardGroupUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(technicalCardGroupDeleteCall, TechnicalCardGroupDto.class, id);
    }
}
