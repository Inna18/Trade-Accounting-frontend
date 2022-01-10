package com.trade_accounting.components.purchases.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.WarehouseService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintSupplierXls extends PrintExcelDocument<SupplierAccountDto> {
    private final ContractorService contractorService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final List<String> sumList;
    private int lengthOfsumList = 0;


    public PrintSupplierXls(String pathToXlsTemplate, List<SupplierAccountDto> list,
                            ContractorService contractorService, WarehouseService warehouseService, CompanyService companyService, List<String> sumList, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.sumList = sumList;
    }

    @Override
    public void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDate.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue(employeeService.getPrincipal().getEmail());
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, SupplierAccountDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<contractor>"):
                editCell.setCellValue(contractorService.getById(model.getContractorId()).getName());
                break;
            case ("<company>"):
                editCell.setCellValue(companyService.getById(model.getCompanyId()).getName());
                break;
            case ("<warehouse>"):
                editCell.setCellValue(warehouseService.getById(model.getWarehouseId()).getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(sumList.get(lengthOfsumList++));
                if (lengthOfsumList >= sumList.size()) {
                    lengthOfsumList = 0;
                }
                break;
            case ("<isSpend>"):
                if (model.getIsSpend()) {
                    editCell.setCellValue("+");
                } else {
                    editCell.setCellValue("-");
                }
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}