package com.trade_accounting.components.purchases.print;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintInvoicesXls extends PrintExcelDocument<InvoiceDto> {

    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final List<String> sumList;
    private int lengthOfsumList = 0;


    public PrintInvoicesXls(String pathToXlsTemplate, List<InvoiceDto> list,
                            ContractorService contractorService, CompanyService companyService, List<String> sumList, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.sumList = sumList;
    }

    @Override
    protected void selectValue(Cell editCell) {
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
    protected void tableSelectValue(String value, InvoiceDto model, Cell editCell) {
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
            case ("<isSpend>"):
                if (model.getIsSpend()) {
                    editCell.setCellValue("+");
                } else {
                    editCell.setCellValue("-");
                }
                break;
            case ("<sum>"):
                editCell.setCellValue(sumList.get(lengthOfsumList++));
                if (lengthOfsumList >= sumList.size()) {
                    lengthOfsumList = 0;
                }
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}
