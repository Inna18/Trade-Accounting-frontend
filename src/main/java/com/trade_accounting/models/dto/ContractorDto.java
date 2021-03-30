package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String contractorGroupName;
    private long contractorGroupId;

    @EqualsAndHashCode.Exclude
    private TypeOfContractorDto typeOfContractorDto;
    private String typeOfContractorName;
    private long typeOfContractorId;

    @EqualsAndHashCode.Exclude
    private TypeOfPriceDto typeOfPriceDto;
    private String typeOfPriceName;
    private long typeOfPriceId;

    @EqualsAndHashCode.Exclude
    private List<BankAccountDto> bankAccountDto;

    @EqualsAndHashCode.Exclude
    private LegalDetailDto legalDetailDto;
    private String legalDetailInn;
    private long legalDetailId;


}
