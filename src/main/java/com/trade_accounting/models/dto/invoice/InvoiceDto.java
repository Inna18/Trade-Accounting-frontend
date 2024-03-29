package com.trade_accounting.models.dto.invoice;

import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;

    private String date;

    private String typeOfInvoice;

    private Long companyId;

    private Long contractorId;

    private Long warehouseId;

    private List<Long> invoiceProductsIds;

    private Boolean isSpend;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;

    private Long invoicesStatusId;

    private Boolean isRecyclebin;

    private Long projectId;

    private Long salesChannelId;

    @Override
    public String toString() {
        return  "Заказ № = " + id +
                ", Дата ='" + date + '\'' +
                ", typeOfInvoice ='" + typeOfInvoice + '\'' +
                ", Организация = " + companyId +
                ", Контрагент = " + contractorId +
                ", Склад = " + warehouseId +
                ", Проведено = " + isSpend +
                ", Отправлено = " + isSent +
                ", Напечатано = " + isPrint +
                ", Комментарий = '" + comment + '\'' +
                ", Проект = " + projectId +
                ", Канал продаж = " + salesChannelId;
    }

    @EqualsAndHashCode.Exclude
    private WarehouseDto warehouseDto;

    @EqualsAndHashCode.Exclude
    private ContractorDto contractorDto;

    @EqualsAndHashCode.Exclude
    private CompanyDto companyDto;

    @EqualsAndHashCode.Exclude
    private SalesChannelDto salesChannelDto;
}
