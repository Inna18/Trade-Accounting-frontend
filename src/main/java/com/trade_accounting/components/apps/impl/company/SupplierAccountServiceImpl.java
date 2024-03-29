package com.trade_accounting.components.apps.impl.company;

import com.trade_accounting.models.dto.company.SupplierAccountDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.company.SupplierAccountService;
import com.trade_accounting.services.api.company.SupplierAccountApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SupplierAccountServiceImpl implements SupplierAccountService {

    private final SupplierAccountApi supplier;
    private final String supplierUrl;
    private final CallExecuteService<SupplierAccountDto> callExecuteService;



    public SupplierAccountServiceImpl(@Value("${supplier_account_url}") String supplierUrl, Retrofit retrofit, CallExecuteService<SupplierAccountDto> callExecuteService) {
        supplier = retrofit.create(SupplierAccountApi.class);
        this.supplierUrl = supplierUrl;
        this.callExecuteService = callExecuteService;

    }

    @Override
    public List<SupplierAccountDto> getAll() {
        List<SupplierAccountDto> getAllSupplierAccount = new ArrayList<>();
        Call<List<SupplierAccountDto>> getAllSupplierAccountCall = supplier.getAll(supplierUrl);

        try {
            getAllSupplierAccount = getAllSupplierAccountCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка SupplierAccountsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка SupplierAccountsDto - {IOException}", e);
        }
        return getAllSupplierAccount;
    }

    @Override
    public List<SupplierAccountDto> getAll(String typeOfInvoice) {
        List<SupplierAccountDto> invoiceDtoList = new ArrayList<>();
        Call<List<SupplierAccountDto>> invoiceDtoListCall = supplier.getAll(supplierUrl, typeOfInvoice);
        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка SupplierAccountDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка SupplierAccountDto - {IOException}", e);
        }
        invoiceDtoList.removeIf(supp -> Boolean.TRUE.equals(supp.getIsRecyclebin()));
        return invoiceDtoList;

    }

    @Override
    public SupplierAccountDto getById(Long id) {
        Call<SupplierAccountDto> getSupplierAccountByIdCall = supplier.getById(supplierUrl, id);
        SupplierAccountDto supplierAccountDto = new SupplierAccountDto();
        try {
            supplierAccountDto = getSupplierAccountByIdCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счета поставщика supplierAccount по его id {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение счета поставщика SupplierAccount" +
                    " по его id {} - {IOException}", id, e);
        }
        return supplierAccountDto;
    }

    @Override
    public List<SupplierAccountDto> searchByString(String nameFilter) {
        Call<List<SupplierAccountDto>> getSupplierAccountByNameFilter = supplier.searchByString(supplierUrl, nameFilter);
        List<SupplierAccountDto> supplierAccountDto = new ArrayList<>();
        try {
            supplierAccountDto = getSupplierAccountByNameFilter.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счета поставщика  по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск иполучение счета поставщика {IOException}", e);
        }
        return supplierAccountDto;
    }

    @Override
    public Response<SupplierAccountDto> create(SupplierAccountDto supplierAccountDto) {
        Call<SupplierAccountDto> createSupplierAccount = supplier.create(supplierUrl, supplierAccountDto);
        Response<SupplierAccountDto> response = Response.success(new SupplierAccountDto());
        try {
            response = createSupplierAccount.execute();
            log.info("Успешно выполнен запрос на создание supplierAccount");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании счета поставщика {}", e);
        }
        return response;
    }

    @Override
    public void update(SupplierAccountDto supplierAccountDto) {
        Call<Void> updateSupplierAccount = supplier.update(supplierUrl, supplierAccountDto);
        try {
            updateSupplierAccount.execute().body();
            log.info("Успешно выполнен запрос на обновление счета поставщика {}", supplierAccountDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление счета поставщика {}", supplierAccountDto);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> deleteSupplierAccountById = supplier.deleteById(supplierUrl,id);
        try {
            deleteSupplierAccountById.execute();
            log.info("Успешно выполнен запрос на удаление SupplierAccount  {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление SupplierAccount {} - {}",id, e);
        }
    }

    @Override
    public List<SupplierAccountDto> searchByFilter(Map<String, String> querySupplier) {
        List<SupplierAccountDto> supplierAccountDtoList = new ArrayList<>();
        Call<List<SupplierAccountDto>> callSupplier = supplier.searchByFilter(supplierUrl,querySupplier);
        try {
            supplierAccountDtoList = callSupplier.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счета поставщика  по фильтру {}",querySupplier);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение счета поставщика {IOException}", e);
        }
        supplierAccountDtoList.removeIf(supp -> Boolean.TRUE.equals(supp.getIsRecyclebin()));
        return supplierAccountDtoList;
    }

    @Override
    public List<SupplierAccountDto> findBySearchAndTypeOfInvoice(String search, String typeOfInvoice) {
        List<SupplierAccountDto> supplierAccountDtoList = new ArrayList<>();
        Call<List<SupplierAccountDto>> callSupplier = supplier
                .search(supplierUrl, search.toLowerCase(), typeOfInvoice);

        try {
            supplierAccountDtoList = callSupplier.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return supplierAccountDtoList;
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = supplier.moveToIsRecyclebin(supplierUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, SupplierAccountDto.class, id);

    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = supplier.restoreFromIsRecyclebin(supplierUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, SupplierAccountDto.class, id);

    }
}
