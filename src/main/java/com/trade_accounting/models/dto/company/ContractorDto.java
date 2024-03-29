package com.trade_accounting.models.dto.company;

import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
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
    private String shortname;
    private String sortNumber;
    private String phone;
    private String fax;
    private String email;

    private Long addressId;

    private String commentToAddress;
    private String comment;
    private String discountCardNumber;

    private List<Long> contactIds;
    private Long contractorGroupId;
    private Long typeOfPriceId;
    private List<Long> bankAccountIds;
    private Long legalDetailId;
    private Long contractorStatusId;
    private Long accessParametersId;

//    private Long id;
//    private String name;
//    private String sortNumber;
//    private String phone;
//    private String fax;
//    private String email;

//    @EqualsAndHashCode.Exclude
//    private AddressDto addressDto;

//    private String commentToAddress;
//    private String comment;
//    private String discountCardNumber;

//    @EqualsAndHashCode.Exclude
//    private List<ContactDto> contactDto;
    @EqualsAndHashCode.Exclude
    private ContractorGroupDto contractorGroupDto;
    @EqualsAndHashCode.Exclude
    private TypeOfPriceDto typeOfPriceDto;
    @EqualsAndHashCode.Exclude
    private EmployeeDto employeeDto;
    @EqualsAndHashCode.Exclude
    private DepartmentDto departmentDto;
//    @EqualsAndHashCode.Exclude
//    private List<BankAccountDto> bankAccountDto;
//    @EqualsAndHashCode.Exclude
//    private LegalDetailDto legalDetailDto;
    @EqualsAndHashCode.Exclude
    private ContractorStatusDto contractorStatusDto;
//    @EqualsAndHashCode.Exclude
//    private AccessParametersDto accessParametersDto;

    private Long accountId;
}
