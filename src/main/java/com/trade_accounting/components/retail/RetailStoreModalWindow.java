package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RetailStoreModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final Checkbox isActive = new Checkbox();
    private final TextField name = new TextField();
    private final Select<CompanyDto> organization = new Select<>();
    private final TextField salesInvoicePrefix = new TextField();
    private final Select<String> defaultTaxationSystem = new Select<>();
    private final Select<String> orderTaxationSystem = new Select<>();
    private final MultiSelectListBox<EmployeeDto> cashiers = new MultiSelectListBox<>();

    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private RetailStoreDto retailStoreDtoToEdit = new RetailStoreDto();
    private Binder<RetailStoreDto> retailStoreDtoBinder = new Binder<>(RetailStoreDto.class);

    public RetailStoreModalWindow(RetailStoreService retailStoreService, CompanyService companyService,
                                  EmployeeService employeeService, PositionService positionService) {
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.positionService = positionService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Точка продаж");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addActiveCheck(),
                addStoreName(),
                addOrganization(),
                addPrefix(),
                addDefaultTaxation(),
                addOrderTaxation(),
                addCashiers()
        );
        layout.add(div);
        return layout;
    }

    private Component addActiveCheck() {
        Label label = new Label("Включена");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, isActive);
    }

    private Component addStoreName() {
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        retailStoreDtoBinder.forField(name).
                withValidator(text -> text.length() >= 1, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("name");
        name.setWidth(fieldWidth);
        return new HorizontalLayout(label, name);
    }

    private Component addOrganization() {
        Label label = new Label("Организация");
        label.setWidth(labelWidth);
        organization.setItemLabelGenerator(CompanyDto::getName);
        retailStoreDtoBinder.forField(organization).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("companyDto");
        organization.setWidth(fieldWidth);
        return new HorizontalLayout(label, organization);
    }

    private Component addPrefix() {
        Label label = new Label("Префикс номера продаж");
        label.setWidth(labelWidth);
        salesInvoicePrefix.setWidth(fieldWidth);
        return new HorizontalLayout(label, salesInvoicePrefix);
    }

    private Component addDefaultTaxation() {
        Label label = new Label("Система налогообложения по умолчанию");
        label.setWidth(labelWidth);
        defaultTaxationSystem.setWidth(fieldWidth);
        defaultTaxationSystem.setItems("ОСН", "УСН. Доход", "УСН. Доход-Расход", "ЕСХН", "ЕНВД", "Патент");
        return new HorizontalLayout(label, defaultTaxationSystem);
    }

    private Component addOrderTaxation() {
        Label label = new Label("Система налогообложения для заказов");
        label.setWidth(labelWidth);
        orderTaxationSystem.setWidth(fieldWidth);
        if (defaultTaxationSystem.getValue() != null) {
            orderTaxationSystem.setValue(defaultTaxationSystem.getValue());
        }
        orderTaxationSystem.setItems("ОСН", "УСН. Доход", "УСН. Доход-Расход", "ЕСХН", "ЕНВД", "Патент");
        return new HorizontalLayout(label, orderTaxationSystem);
    }

    private Component addCashiers() {
        Label label = new Label("Кассиры");
        label.setWidth(labelWidth);
        List<EmployeeDto> cashierEmployees = employeeService.getAll().stream().
                filter(e -> (positionService.getById(e.getPositionDtoId()).getName().equals("Кассир"))).collect(Collectors.toList());
        cashiers.setItems(cashierEmployees);
        cashiers.setWidth(fieldWidth);
        return new HorizontalLayout(label, cashiers);
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (retailStoreDtoToEdit.getId() != null) {
                retailStoreDtoToEdit.setIsActive(isActive.getValue());
                retailStoreDtoToEdit.setName(name.getValue());
                retailStoreDtoToEdit.setActivityStatus("Онлайн");
                retailStoreDtoToEdit.setRevenue(new BigDecimal(0_00));
                retailStoreDtoToEdit.setCompanyId(organization.getValue().getId());
                retailStoreDtoToEdit.setSalesInvoicePrefix(salesInvoicePrefix.getValue());
                retailStoreDtoToEdit.setDefaultTaxationSystem(defaultTaxationSystem.getValue());
                retailStoreDtoToEdit.setOrderTaxationSystem(orderTaxationSystem.getValue());
                if (cashiers.isEmpty()) {
                    cashiers.setValue(retailStoreDtoToEdit.getCashiersIds().stream().map(employeeService::getById).collect(Collectors.toSet()));
                }
                retailStoreDtoToEdit.setCashiersIds(cashiers.getValue().stream().map(EmployeeDto::getId).collect(Collectors.toList()));
                if (retailStoreDtoBinder.validate().isOk()) {
                    retailStoreService.update(retailStoreDtoToEdit);
                    clearAll();
                    close();
                } else {
                    retailStoreDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                RetailStoreDto retailStoreDto = new RetailStoreDto();
                retailStoreDto.setIsActive(isActive.getValue());
                retailStoreDto.setName(name.getValue());
                retailStoreDto.setActivityStatus("Онлайн");
                retailStoreDto.setRevenue(new BigDecimal(0_00));
                retailStoreDto.setCompanyId(organization.getValue().getId());
                retailStoreDto.setSalesInvoicePrefix(salesInvoicePrefix.getValue());
                retailStoreDto.setDefaultTaxationSystem(defaultTaxationSystem.getValue());
                retailStoreDto.setOrderTaxationSystem(orderTaxationSystem.getValue());
                retailStoreDto.setCashiersIds(cashiers.getValue().stream().map(EmployeeDto::getId).collect(Collectors.toList()));
                if (retailStoreDtoBinder.validate().isOk()) {
                    retailStoreService.create(retailStoreDto);
                    clearAll();
                    close();
                } else {
                    retailStoreDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearAll();
            close();
        });
    }

    public void setRetailStoreDataEdit(RetailStoreDto retailStoreDto) {
        retailStoreDtoToEdit = retailStoreDto;
        if (retailStoreDtoToEdit.getIsActive()) {
            isActive.setValue(true);
        }
        if (retailStoreDtoToEdit.getName() != null) {
            name.setValue(retailStoreDto.getName());
        }
        if (retailStoreDtoToEdit.getCompanyId() != null) {
            organization.setValue(companyService.getById(retailStoreDto.getCompanyId()));
        }
        if (retailStoreDtoToEdit.getSalesInvoicePrefix() != null) {
            salesInvoicePrefix.setValue(retailStoreDto.getSalesInvoicePrefix());
        }
        if (retailStoreDtoToEdit.getDefaultTaxationSystem() != null) {
            defaultTaxationSystem.setValue(retailStoreDto.getDefaultTaxationSystem());
        }
        if (retailStoreDtoToEdit.getOrderTaxationSystem() != null) {
            orderTaxationSystem.setValue(retailStoreDto.getOrderTaxationSystem());
        }

        if (retailStoreDtoToEdit.getCashiersIds() != null) {
            cashiers.setValue(retailStoreDto.getCashiersIds().stream().map(employeeService::getById).collect(Collectors.toSet()));
        }
    }

    public void clearAll() {
        isActive.clear();
        name.clear();
        organization.clear();
        salesInvoicePrefix.clear();
        defaultTaxationSystem.clear();
        orderTaxationSystem.clear();
        cashiers.clear();
    }
}
