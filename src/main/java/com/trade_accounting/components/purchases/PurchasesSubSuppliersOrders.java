package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Route(value = "suppliersOrders", layout = AppView.class)
@PageTitle("Заказы поставщикам")
public class PurchasesSubSuppliersOrders extends VerticalLayout {


    private final InvoiceService invoiceService;

    private List<InvoiceDto> invoices;


    private HorizontalLayout actions;
    private Grid<InvoiceDto> grid = new Grid<>(InvoiceDto .class, false);
    private GridPaginator<InvoiceDto> paginator;

    public PurchasesSubSuppliersOrders(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;

        loadInvoices();

        configureActions();
        configureGrid();
        configurePaginator();

        add(actions, grid, paginator);
    }

    private void loadInvoices() {
        invoices = invoiceService.getAll();
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private Grid<InvoiceDto> configureGrid() {
        Icon check = new Icon(VaadinIcon.CHECK);
        check.setColor("green");
        check.getElement().setAttribute("title", "Проведена");
        grid.setColumns("spend", "id", "date", "typeOfInvoice");
        grid.getColumnByKey("spend").setHeader(check);
        grid.getColumnByKey("id").setHeader("id");
        grid.getColumnByKey("date").setHeader("Дата").setFlexGrow(10);
        grid.getColumnByKey("typeOfInvoice").setHeader("Счет-фактура").setFlexGrow(4);
        grid.addColumn(iDto -> iDto.getCompanyDto().getName()).setSortable(true).setHeader("Компания")
                .setFlexGrow(10).setId("companyDto");
        grid.addColumn(iDto -> iDto.getContractorDto().getName()).setSortable(true).setHeader("Контрагент")
                .setFlexGrow(10).setId("contractorDto");
        grid.addColumn(iDto -> iDto.getWarehouseDto().getName()).setSortable(true).setHeader("WarehouseDto")
                .setFlexGrow(10).setId("warehouseDto");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, invoices, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title() {
        H2 title = new H2("Заказы поставщикам");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private TextField filterTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valueCreate() {
        Select<String> create = new Select<>();
        create.setItems("Создать");
        create.setValue("Создать");
        create.setWidth("130px");
        return create;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }
}
