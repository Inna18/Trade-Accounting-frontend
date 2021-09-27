package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.RetailMakingDto;
import com.trade_accounting.models.dto.RetailShiftDto;
import com.trade_accounting.services.interfaces.RetailShiftService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Route(value = "RetailShiftView", layout = AppView.class)
@PageTitle("Внесения")
@SpringComponent
@UIScope
public class RetailShiftView extends VerticalLayout implements AfterNavigationObserver {
    private final RetailShiftService retailShiftService;
    private List<RetailShiftDto> data;

    private final GridFilter<RetailShiftDto> filter;
    private final Grid<RetailShiftDto> grid = new Grid<>(RetailShiftDto.class, false);
    private final GridPaginator<RetailShiftDto> paginator;

    public RetailShiftView(RetailShiftService retailShiftService) {
        this.retailShiftService = retailShiftService;
        this.data = retailShiftService.getAll();
        grid.addColumn("dataOpen").setFlexGrow(10).setHeader("Дата открытия").setId("Дата открытия");
        grid.addColumn("dataClose").setFlexGrow(10).setHeader("Дата закрытия").setId("Дата закрытия");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("warehouseId").setFlexGrow(5).setHeader("Склад").setId("Склад");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("Организация");
        grid.addColumn("bank").setFlexGrow(5).setHeader("Банк-эквайер").setId("Банк-эквайер");
        grid.addColumn("revenuePerShift").setFlexGrow(5).setHeader("Выручка за смену").setId("Выручка за смену");
        grid.addColumn("received").setFlexGrow(5).setHeader("Поступило").setId("Поступило");
        grid.addColumn("amountOfDiscounts").setFlexGrow(5).setHeader("Сумма скидок").setId("Сумма скидок");
        grid.addColumn("commission_amount").setFlexGrow(5).setHeader("Сумма комиссии").setId("Сумма комиссии");
        grid.addColumn("comment").setFlexGrow(5).setHeader("Комментарий").setId("Комментарий");
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn("dataOpen").setFlexGrow(10).setHeader("Дата открытия").setId("dataOpen");
        grid.addColumn("dataClose").setFlexGrow(10).setHeader("Дата закрытия").setId("dataClose");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn("warehouseId").setFlexGrow(5).setHeader("Склад").setId("warehouseId");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("companyId");
        grid.addColumn("bank").setFlexGrow(5).setHeader("Банк-эквайер").setId("bank");
        grid.addColumn("revenuePerShift").setFlexGrow(5).setHeader("Выручка за смену").setId("revenuePerShift");
        grid.addColumn("received").setFlexGrow(5).setHeader("Поступило").setId("received");
        grid.addColumn("amountOfDiscounts").setFlexGrow(5).setHeader("Сумма скидок").setId("amountOfDiscounts");
        grid.addColumn("commission_amount").setFlexGrow(5).setHeader("Сумма комиссии").setId("commission_amount");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("comment");

        GridSortOrder<RetailShiftDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component isSentCheckedIcon(RetailShiftDto retailShiftDto) {
        if (retailShiftDto.getSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailShiftDto retailShiftDto) {
        if (retailShiftDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), numberField(), textField(), getSelect(),getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Внесения");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void updateList() {
        GridPaginator<RetailShiftDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailShiftService.getAll(), 100);
        GridSortOrder<RetailShiftDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }


    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }


    private Select<String> getSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }


    private Select<String> getPrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
