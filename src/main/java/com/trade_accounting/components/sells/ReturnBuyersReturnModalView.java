package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UIScope
@SpringComponent
public class ReturnBuyersReturnModalView extends Dialog {

    private final ContractorService contractorService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Binder<BuyersReturnDto> buyersReturnDtoBinder = new Binder<>(BuyersReturnDto.class);
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    public  final  ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final Checkbox checkboxIsSent = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField summConfig = new TextField();
    private final TextField commentConfig = new TextField();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final ComboBox<String> choosePromoCode = new ComboBox<>();
    private       String datePeriod;
    private final DatePicker dateOn = new DatePicker();
    private final DatePicker dateTo = new DatePicker();
    private BuyersReturnDto buyersReturnDto = new BuyersReturnDto();
    private final BuyersReturnService buyersReturnService;
    private final TextField buyersNumber = new TextField();
    private final Notifications notifications;
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public ReturnBuyersReturnModalView(BuyersReturnService buyersReturnService,
                                       ContractorService contractorService,
                                       WarehouseService warehouseService,
                                          CompanyService companyService,
                                       Notifications notifications) {
        this.buyersReturnService = buyersReturnService;
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.notifications = notifications;
        setSizeFull();

        add(topButtons(), formToAddCommissionAgent());
    }

    private void updateSupplier() {
        buyersReturnDto.setId(Long.parseLong(buyersNumber.getValue()));
        buyersReturnDto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
        buyersReturnDto.setCompanyId(companyDtoComboBox.getValue().getId());
        buyersReturnDto.setContractorId(contractorDtoComboBox.getValue().getId());
        buyersReturnDto.setComment(commentConfig.getValue());
        buyersReturnDto.setDate(dateTimePicker.getValue().toString());
        buyersReturnDto.setSum(new BigDecimal(summConfig.getValue()));
        buyersReturnDto.setIsSent(checkboxIsSent.getValue());
        buyersReturnDto.setIsPrint(checkboxIsPrint.getValue());
        buyersReturnService.update(buyersReturnDto);
        UI.getCurrent().navigate("buyersReturns");
        close();
    }

    private Button save() {
        Button button = new Button("Сохранить");
        button.addClickListener(e -> {
            if (!buyersReturnDtoBinder.validate().isOk()) {
                buyersReturnDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                updateSupplier();
                notifications.infoNotification(String.format("Возврат покупателя № %s сохранен", buyersReturnDto.getId()));
            }
        });
        return button;
    }

    public void setReturnEdit(BuyersReturnDto editDto) {
        this.buyersReturnDto = editDto;
        buyersNumber.setValue(editDto.getId().toString());
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
        commentConfig.setValue(editDto.getComment());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        summConfig.setValue(String.valueOf(editDto.getSum()));
    }

    private HorizontalLayout topButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        setSizeFull();
        horizontalLayout.add(save(), closeButton(), addProductButton(), checkboxIsSent, checkboxIsPrint);
        return horizontalLayout;
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;

    }

    private Button addProductButton() {
        return new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE), e -> {
        });
    }

    private VerticalLayout formToAddCommissionAgent() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(),
                horizontalLayout3(), horizontalLayout2(), horizontalLayout4(), commentConfigure());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.add(dataConfigure());
        return hLayout;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Возврат покупателя №");
        label.setWidth("150px");
        buyersNumber.setAutofocus(true);
        buyersNumber.setWidth("50px");
        buyersNumber.setRequired(true);
        buyersNumber.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(buyersNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getIdValid, BuyersReturnDto::setIdValid);
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
        dateTimePicker.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getDateValid, BuyersReturnDto::setDateValid);
        horizontalLayout.add(label, buyersNumber, label2, dateTimePicker);
        return horizontalLayout;
    }

//    private HorizontalLayout horizontalLayout2() {
//        HorizontalLayout hLay2 = new HorizontalLayout();
//        Label label = new Label("Период");
//        hLay2.add(companyConfigure(), label,dateOn,dateTo);
//        return hLay2;
//    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        hLay2.add(warehouseConfigure());
        return hLay2;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouseDtos = warehouseService.getAll();
        if (warehouseDtos != null) {
            warehouseDtoComboBox.setItems(warehouseDtos);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        warehouseDtoComboBox.setRequired(true);
        warehouseDtoComboBox.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getWarehouseDtoValid, BuyersReturnDto::setWarehouseDtoValid);
        Label label = new Label("На склад");
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(companyConfigure(),contractorConfigure());
        return hLay3;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if (companyDtos != null) {
            companyDtoComboBox.setItems(companyDtos);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        companyDtoComboBox.setRequired(true);
        companyDtoComboBox.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getCompanyDtoValid, BuyersReturnDto::setCompanyDtoValid);
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if (contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        contractorDtoComboBox.setRequired(true);
        contractorDtoComboBox.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getContractorDtoValid, BuyersReturnDto::setContractorDtoValid);
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout hLayout4 = new HorizontalLayout();
        hLayout4.add(summConfigure(), configureAward());
        return hLayout4;
    }

    private HorizontalLayout summConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();
        summConfig.setWidth("200px");
        summConfig.setRequired(true);
        summConfig.setRequiredIndicatorVisible(true);
        buyersReturnDtoBinder.forField(summConfig)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BuyersReturnDto::getIdValid, BuyersReturnDto::setIdValid);
        Label label = new Label("Сумма");
        label.setWidth("100px");
        horizontal1.add(label, summConfig);
        return horizontal1;
    }

    private HorizontalLayout configureAward() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        choosePromoCode.setItems("Выдать купон","Не выдавать купон");
        choosePromoCode.setValue("Выдать купон");
        Label label = new Label("Компенсация");
        horizontalLayout.add(label, choosePromoCode);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
        commentConfig.setWidth("500px");
        commentConfig.setHeight("300px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }

}
