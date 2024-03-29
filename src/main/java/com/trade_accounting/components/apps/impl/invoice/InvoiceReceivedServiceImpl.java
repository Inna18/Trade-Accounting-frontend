package com.trade_accounting.components.apps.impl.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.invoice.InvoiceReceivedService;
import com.trade_accounting.services.api.invoice.InvoiceReceivedApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class InvoiceReceivedServiceImpl implements InvoiceReceivedService {

    private final InvoiceReceivedApi invoiceReceivedApi;

    private final String invoiceReceivedUrl;

    private final CallExecuteService<InvoiceReceivedDto> dtoCallExecuteService;

    public InvoiceReceivedServiceImpl(Retrofit retrofit, @Value("${invoice_received_url}") String invoiceReceivedUrl,
                                      CallExecuteService<InvoiceReceivedDto> dtoCallExecuteService) {
        this.invoiceReceivedApi = retrofit.create(InvoiceReceivedApi.class);
        this.invoiceReceivedUrl = invoiceReceivedUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceReceivedDto> getAll() {
        Call<List<InvoiceReceivedDto>> listCall = invoiceReceivedApi.getAll(invoiceReceivedUrl);
        return dtoCallExecuteService.callExecuteBodyList(listCall, InvoiceReceivedDto.class);
    }

    @Override
    public List<InvoiceReceivedDto> getAll(String typeOfInvoice) {
        List<InvoiceReceivedDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceReceivedDto>> invoiceDtoListCall = invoiceReceivedApi.getAll(invoiceReceivedUrl);
        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка ShipmentDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка ShipmentDto - {IOException}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public List<InvoiceReceivedDto> searchByFilter(Map<String, String> queryInvoiceReceived) {
        List<InvoiceReceivedDto> invoiceReceivedDtoList = new ArrayList<>();
        Call<List<InvoiceReceivedDto>> callInvoiceReceived = invoiceReceivedApi.searchByFilter(invoiceReceivedUrl, queryInvoiceReceived);
        try {
            invoiceReceivedDtoList = callInvoiceReceived.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счета фактуры полученные по фильтру {}", queryInvoiceReceived);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение счета фактуры полученные {IOException}", e);
        }
        return invoiceReceivedDtoList;
    }

    @Override
    public List<InvoiceReceivedDto> searchByString(String nameFilter) {
        List<InvoiceReceivedDto> invoiceReceivedDtoList = new ArrayList<>();
        Call<List<InvoiceReceivedDto>> getInvoiceReceivedByNameFilter = invoiceReceivedApi.searchByString(invoiceReceivedUrl, nameFilter);
        try {
            invoiceReceivedDtoList = getInvoiceReceivedByNameFilter.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение отгрузки по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение отгрузки {IOException}", e);
        }
        return invoiceReceivedDtoList;
    }

    @Override
    public InvoiceReceivedDto getById(Long id) {
        Call<InvoiceReceivedDto> invoiceReceivedDtoCall = invoiceReceivedApi.getById(invoiceReceivedUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceReceivedDtoCall, InvoiceReceivedDto.class, id);
    }

    @Override
    public void create(InvoiceReceivedDto invoiceReceivedDto) {
        Call<Void> voidCall = invoiceReceivedApi.create(invoiceReceivedUrl, invoiceReceivedDto);
        dtoCallExecuteService.callExecuteBodyCreate(voidCall, InvoiceReceivedDto.class);

    }

    @Override
    public void update(InvoiceReceivedDto invoiceReceivedDto) {
        Call<Void> voidCall = invoiceReceivedApi.update(invoiceReceivedUrl, invoiceReceivedDto);
        dtoCallExecuteService.callExecuteBodyUpdate(voidCall, InvoiceReceivedDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> voidCall = invoiceReceivedApi.deleteById(invoiceReceivedUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(voidCall, InvoiceReceivedDto.class, id);
    }

}
