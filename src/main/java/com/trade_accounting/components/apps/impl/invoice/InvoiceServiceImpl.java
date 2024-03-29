package com.trade_accounting.components.apps.impl.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.purchases.PurchaseCreateOrderDto;
import com.trade_accounting.services.api.invoice.InvoiceApi;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceApi invoiceApi;

    private final String invoiceUrl;

    private final CallExecuteService<InvoiceDto> dtoCallExecuteService;

    public InvoiceServiceImpl(@Value("${invoice_url}") String invoiceUrl, Retrofit retrofit, CallExecuteService<InvoiceDto> dtoCallExecuteService) {
        invoiceApi = retrofit.create(InvoiceApi.class);
        this.invoiceUrl = invoiceUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceDto> getAll() {
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl);
        return dtoCallExecuteService.callExecuteBodyList(invoiceDtoListCall, InvoiceDto.class);
    }

    @Override
    public List<InvoiceDto> getAll(String typeOfInvoice) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl, typeOfInvoice);

        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка InvoiceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public List<InvoiceDto> getByContractorId(Long id) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getByContractorId(invoiceUrl, id);

        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка InvoiceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public List<InvoiceDto> getByProjectId(Long id) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getByProjectId(invoiceUrl, id);

        try {
//            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto getById(Long id) {
        Call<InvoiceDto> invoiceDtoCall = invoiceApi.getById(invoiceUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceDtoCall, InvoiceDto.class, id);
    }

    @Override
    public List<InvoiceDto> search(Map<String, String> query) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.search(invoiceUrl, query);
        try {
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }


    @Override
    public List<InvoiceDto> searchFromDate(LocalDateTime dateTime) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> listCall = invoiceApi.searchFromDate(invoiceUrl, dateTime.toString());
        try {
            invoiceDtoList = listCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice {}", dateTime.toString());
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }


    @Override
    public List<InvoiceDto> findBySearchAndTypeOfInvoice(String search, String typeOfInvoice) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi
                .search(invoiceUrl, search.toLowerCase(), typeOfInvoice);

        try {
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }

    @Override
    public Response<InvoiceDto> create(InvoiceDto invoiceDto) {

        Call<InvoiceDto> invoiceDtoCall = invoiceApi.create(invoiceUrl, invoiceDto);
//        Response<InvoiceDto> resp = Response.success(new InvoiceDto());
        Response<InvoiceDto> resp = Response.success(invoiceDto);

        try {
            resp = invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание Invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение ProductDto - {}", e);
        }

        return resp;
    }

    @Override
    public void update(InvoiceDto invoiceDto) {
        Call<Void> invoiceDtoCall = invoiceApi.update(invoiceUrl, invoiceDto);
        dtoCallExecuteService.callExecuteBodyUpdate(invoiceDtoCall, InvoiceDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceDtoCall = invoiceApi.deleteById(invoiceUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(invoiceDtoCall, InvoiceDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = invoiceApi.moveToIsRecyclebin(invoiceUrl, id);
        dtoCallExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, InvoiceDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = invoiceApi.restoreFromIsRecyclebin(invoiceUrl, id);
        dtoCallExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, InvoiceDto.class, id);

    }

    @Override
    public void createAll(PurchaseCreateOrderDto purchaseCreateOrderDto) {
        Call<Void> dtoCall = invoiceApi.createAll(invoiceUrl, purchaseCreateOrderDto);
        dtoCallExecuteService.callExecuteBodyCreateAll(dtoCall, InvoiceDto.class);
    }
}
