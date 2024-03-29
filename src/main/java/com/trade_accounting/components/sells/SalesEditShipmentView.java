package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.profile.SalesChannelModalWindow;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.models.dto.warehouse.ShipmentDto;
import com.trade_accounting.models.dto.warehouse.ShipmentProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentProductService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.SELLS_SELLS__SHIPMENT_EDIT;

@Slf4j
@Route(value = SELLS_SELLS__SHIPMENT_EDIT, layout = AppView.class)
@PageTitle("Добавить отгрузку")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class SalesEditShipmentView extends VerticalLayout{
    private final SalesSubMenuView salesSubMenuView;
    private final ProductService productService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final WarehouseService warehouseService;
    private final ShipmentService invoiceService;
    private final SalesChannelService salesChannelService;
    private final Notifications notifications;
    private final UnitService unitService;
    private final ShipmentProductService shipmentProductService;
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private static final String SALES_CHANNEL_FIELD_WIDTH = "300px";
    private final TextField invoiceIdField = new TextField();
    private final DateTimePicker dateField = new DateTimePicker();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final ComboBox<CompanyDto> companySelect = new ComboBox<>();
    public final ComboBox<ContractorDto> contractorSelect = new ComboBox<>();
    public final ComboBox<ProjectDto> projectSelect = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelect = new ComboBox<>();
    private final ComboBox<SalesChannelDto> salesChannelSelect = new ComboBox<>();
    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));
    private final H4 totalPrice = new H4();
    private final H2 title = new H2("Добавление отгрузки");
    private List<ShipmentProductDto> tempShipmentProductDtoList = new ArrayList<>();
    private final Dialog dialogOnCloseView = new Dialog();
    private final Grid<ShipmentProductDto> grid = new Grid<>(ShipmentProductDto.class, false);
    private final GridPaginator<ShipmentProductDto> paginator;
    private final Editor<ShipmentProductDto> editor = grid.getEditor();
    private final Binder<ShipmentDto> binderShipmentDto = new Binder<>(ShipmentDto.class);
    private String type = null;
    private String location = null;
    private ShipmentDto dto = new ShipmentDto();

    @Autowired
    public SalesEditShipmentView(ProductService productService,
                                 ContractorService contractorService,
                                 CompanyService companyService,
                                 ProjectService projectService,
                                 WarehouseService warehouseService,
                                 ShipmentService invoiceService,
                                 Notifications notifications,
                                 UnitService unitService,
                                 ShipmentProductService shipmentProductService,
                                 SalesChannelService salesChannelService,
                                 @Lazy SalesSubMenuView salesSubMenuView) {
        this.productService = productService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.projectService = projectService;
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.notifications = notifications;
        this.unitService = unitService;
        this.shipmentProductService = shipmentProductService;
        this.salesChannelService = salesChannelService;
        this.salesSubMenuView = salesSubMenuView;
        configureCloseViewDialog();
        setSizeFull();
        tempShipmentProductDtoList = getData();
        configureGrid();
        paginator = new GridPaginator<>(grid, tempShipmentProductDtoList, 50);
        add(upperButtonsLayout(), formLayout(), grid, paginator);
    }

    private List<ShipmentProductDto> getData() {
        List<ShipmentProductDto> tmp = new ArrayList<>();
        if (dto.getShipmentProductsIds() == null){
            dto.setShipmentProductsIds(new ArrayList<>());
        }
        for (Long shipProductsId : dto.getShipmentProductsIds()){
            tmp.add(shipmentProductService.getById(shipProductsId));
        }
        return tmp;
    }


    private void configureGrid() {
        grid.removeAllColumns();
        grid.setItems(tempShipmentProductDtoList);
        grid.addColumn(inPrDto -> tempShipmentProductDtoList.indexOf(inPrDto) + 1).setHeader("№").setId("№");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        grid.addColumn(ShipmentProductDto::getAmount).setHeader("Количество");
        grid.addColumn(ShipmentProductDto::getPrice).setHeader("Цена").setSortable(true).setId("Цена");
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);

        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);
    }

    public void setReturnToShiptmentForEdit(ShipmentDto editDto) {
        this.dto = editDto;
        tempShipmentProductDtoList = new ArrayList<>();
        if (!dto.getShipmentProductsIds().isEmpty()){
            for (Long shipProductsId : dto.getShipmentProductsIds()) {
                ShipmentProductDto shipmentProductDto = shipmentProductService.getById(shipProductsId);
                tempShipmentProductDtoList.add(shipmentProductDto);
            }
        }
        dateField.setValue(LocalDateTime.parse(editDto.getDate()));
        companySelect.setValue(companyService.getById(editDto.getCompanyId()));
        warehouseSelect.setValue(warehouseService.getById(editDto.getWarehouseId()));
        isSpend.setValue(editDto.getIsSpend());
        contractorSelect.setValue(contractorService.getById(editDto.getContractorId()));
        salesChannelSelect.setValue(salesChannelService.getById(editDto.getSalesChannelId()));
        configureGrid();
    }

    private HorizontalLayout upperButtonsLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonSave(), configureDeleteButton(), buttonClose());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2(),
                horizontalLayout3(),
                horizontalLayout4()
        );
        return upper;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(configureDateField(),
                isSpend
        );
        return horizontalLayout1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(configureCompanySelect(),
                configureWarehouseSelect()
        );
        return horizontalLayout2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.add(configureContractorSelect(),
                configureContractField()
        );
        return horizontalLayout3;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.add(
                configureSalesChannelSelect());
        return horizontalLayout4;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        binderShipmentDto.forField(dateField)
                .asRequired("Не заполнено!")
                .bind(ShipmentDto::getDateValid, ShipmentDto::setDateValid);
        label.setWidth(LABEL_WIDTH);
        dateField.setWidth(FIELD_WIDTH);
        dateField.setHelperText("По умолчанию текущая дата/время");
        horizontalLayout.add(label, dateField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCompanySelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if (companyDtos != null) {
            companySelect.setItems(companyDtos);
        }
        companySelect.setItemLabelGenerator(CompanyDto::getName);
        companySelect.setWidth("350px");
        companySelect.setRequired(true);
        companySelect.setRequiredIndicatorVisible(true);
        binderShipmentDto.forField(companySelect)
                .asRequired("Не заполнено!")
                .bind(ShipmentDto::getCompanyDtoValid, ShipmentDto::setCompanyDtoValid);
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companySelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractors = contractorService.getAll();
        if (contractors != null) {
            contractorSelect.setItems(contractors);
        }
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth(FIELD_WIDTH);
        binderShipmentDto.forField(contractorSelect)
                .asRequired("Не заполнено!")
                .bind(ShipmentDto::getContractorDtoValid, ShipmentDto::setContractorDtoValid);
        Label label = new Label("Контрагент");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Договор");
        TextField textContract = new TextField();
        textContract.setWidth(FIELD_WIDTH);
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, textContract);
        return horizontalLayout;
    }

 private HorizontalLayout configureWarehouseSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouses = warehouseService.getAll();
        if (warehouses != null) {
            warehouseSelect.setItems(warehouses);
        }
        warehouseSelect.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelect.setWidth(FIELD_WIDTH);
        binderShipmentDto.forField(warehouseSelect)
                .asRequired("Не заполнено!")
                .bind(ShipmentDto::getWarehouseDtoValid, ShipmentDto::setWarehouseDtoValid);
        Label label = new Label("Склад");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, warehouseSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureSalesChannelSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<SalesChannelDto> salesChannels = salesChannelService.getAll();
        if (salesChannels != null) {
            salesChannelSelect.setItems(salesChannels);
        }
        salesChannelSelect.setItemLabelGenerator(SalesChannelDto::getName);
        salesChannelSelect.setWidth(SALES_CHANNEL_FIELD_WIDTH);
        Label label = new Label("Канал продаж");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, salesChannelSelect, buttonSalesChannel());
        return horizontalLayout;
    }

    private Button buttonSalesChannel() {
        Button salesChannelButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        salesChannelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        SalesChannelModalWindow salesChannelModalWindow = new SalesChannelModalWindow(new SalesChannelDto(), salesChannelService);
        salesChannelButton.addClickListener(event -> salesChannelModalWindow.open());
//        salesChannelModalWindow.addDetachListener(event -> updateList());
        return salesChannelButton;
    }

    private H2 title() {
        title.setHeight("2.0em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonSave() {
        return new Button("Сохранить", buttonClickEvent -> {
            ShipmentDto invoiceDto = new ShipmentDto();
            if (!binderShipmentDto.validate().isOk()) {
                binderShipmentDto.validate().notifyBindingValidationStatusHandlers();
            } else {

                if (dateField.getValue() == null) {
                    dateField.setValue(LocalDateTime.now());
                }

                if (!invoiceIdField.getValue().equals("")) {
                    invoiceDto.setId(Long.parseLong(invoiceIdField.getValue()));
                }
                invoiceDto.setDate(dateField.getValue().toString());
                invoiceDto.setCompanyId(companySelect.getValue().getId());
                invoiceDto.setContractorId(contractorSelect.getValue().getId());
                invoiceDto.setWarehouseId(warehouseSelect.getValue().getId());
//        invoiceDto.setTypeOfInvoice(type);
                invoiceDto.setIsSpend(isSpend.getValue());
                invoiceDto.setComment("");
                invoiceService.create(invoiceDto);

//                ShipmentDto invoiceDto = saveInvoice(type);
                UI.getCurrent().navigate(location);
                notifications.infoNotification(String.format("Отгрузка № %s сохранена", invoiceDto.getId()));
            }
        });
    }

    private Button buttonClose() {
        Button buttonUnit = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        buttonUnit.addClickListener(event -> {
            dialogOnCloseView.open();
        });
        return buttonUnit;
    }

    private void closeView() {
        resetView();
        UI.getCurrent().navigate(location);
    }

    private Button configureDeleteButton() {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(Long.parseLong(invoiceIdField.getValue()));
            resetView();
            buttonDelete.getUI().ifPresent(ui -> ui.navigate(location));
        });
        return buttonDelete;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (ShipmentProductDto invoiceProductDto : tempShipmentProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(
                String.format("%.2f", getTotalPrice())
        );
    }

    public void resetView() {
        invoiceIdField.clear();
        dateField.clear();
        companySelect.setValue(null);
        contractorSelect.setValue(null);
        projectSelect.setValue(null);
        warehouseSelect.setValue(null);
        isSpend.clear();
        companySelect.setInvalid(false);
        contractorSelect.setInvalid(false);
        projectSelect.setInvalid(false);
        warehouseSelect.setInvalid(false);
        title.setText("Добавление отгрузки");
        setTotalPrice();
    }

//    private ShipmentDto saveInvoice(String type) {
//        ShipmentDto invoiceDto = new ShipmentDto();
//        if (!invoiceIdField.getValue().equals("")) {
//            invoiceDto.setId(Long.parseLong(invoiceIdField.getValue()));
//        }
//        invoiceDto.setDate(dateField.getValue().toString());
//        invoiceDto.setCompanyId(companySelect.getValue().getId());
//        invoiceDto.setContractorId(contractorSelect.getValue().getId());
//        invoiceDto.setWarehouseId(warehouseSelect.getValue().getId());
////        invoiceDto.setTypeOfInvoice(type);
//        invoiceDto.setIsSpend(isSpend.getValue());
//        invoiceDto.setComment("");
//        ShipmentDto invoiceDtoResponse = invoiceService.create(invoiceDto);
//        return invoiceDtoResponse;
//    }

    public void deleteInvoiceById(Long invoiceDtoId) {
        invoiceService.deleteById(invoiceDtoId);
        notifications.infoNotification(String.format("Отгрузка № %s успешно удалена", invoiceDtoId));
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Span message = new Span();

        Button confirmButton = new Button("Продолжить", event -> {
            closeView();
            dialogOnCloseView.close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });
 //Cancel action on ESC press
        Shortcuts.addShortcutListener(dialogOnCloseView, () -> {
            dialogOnCloseView.close();
        }, Key.ESCAPE);

        dialogOnCloseView.add(new Div(confirmButton, new Div(), cancelButton));
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
