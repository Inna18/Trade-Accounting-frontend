package com.trade_accounting.components.apps.impl.finance;

import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.MoneySubProfitLossService;
import com.trade_accounting.services.api.finance.MoneySubProfitLossApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.time.LocalDate;

@Service
@Slf4j
public class MoneySubProfitLossServiceImpl implements MoneySubProfitLossService {

    private final MoneySubProfitLossApi moneySubProfitLossApi;

    private final String moneySubProfitLossUrl;

    private final CallExecuteService<MoneySubProfitLossDto> dtoCallExecuteService;

    public MoneySubProfitLossServiceImpl(@Value("${moneySubProfitLoss_url}") String moneySubProfitLossUrl, Retrofit retrofit, CallExecuteService<MoneySubProfitLossDto> dtoCallExecuteService) {
        moneySubProfitLossApi = retrofit.create(MoneySubProfitLossApi.class);
        this.moneySubProfitLossUrl = moneySubProfitLossUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public MoneySubProfitLossDto getAll() {
        Call<MoneySubProfitLossDto> profitLossDto = moneySubProfitLossApi.getAll(moneySubProfitLossUrl);
        return dtoCallExecuteService.callExecuteBody(profitLossDto, MoneySubProfitLossDto.class);
    }

    @Override
    public void update(MoneySubProfitLossDto moneySubProfitLossDto) {
    }

//    @Override
//    public List<MoneySubProfitLossDto> filter(Map<String, String> query) {
//        return null;
//    }

    @Override
    public MoneySubProfitLossDto filter(LocalDate startDatePeriod, LocalDate endDatePeriod, Long companyId) {
        Call<MoneySubProfitLossDto> paymentDtoListCall = moneySubProfitLossApi.filter(moneySubProfitLossUrl, startDatePeriod, endDatePeriod, companyId);
        return dtoCallExecuteService.callExecuteBody(paymentDtoListCall, MoneySubProfitLossDto.class);
    }
}