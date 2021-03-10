package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ContractorDto {

    private Long id;

    private String name;

    private String inn;

    private String sortNumber;

    private String phone;

    private String fax;

    private String email;

    private String address;

    private String commentToAddress;

    private String comment;

    @EqualsAndHashCode.Exclude
    private ContractorGroupDto contractorGroupDto;

    @EqualsAndHashCode.Exclude
    private TypeOfContractorDto typeOfContractorDto;

    @EqualsAndHashCode.Exclude
    private TypeOfPriceDto typeOfPriceDto;

    @EqualsAndHashCode.Exclude
    private List<BankAccountDto> bankAccountDto;

    @EqualsAndHashCode.Exclude
    private LegalDetailDto legalDetailDto;
}
