package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Boolean isSpend;

    private String comment;
}
