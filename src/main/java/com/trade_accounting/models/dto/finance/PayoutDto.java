package com.trade_accounting.models.dto.finance;

import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayoutDto {

    Long id;

    String date;

    Long retailStoreId;

    String whoWasPaid;

    Long companyId;

    Boolean isSent;

    Boolean isPrint;

    String comment;

    CompanyDto companyDto;

    RetailStoreDto retailStoreDto;
}
