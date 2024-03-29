package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.units.CurrencyDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.retail.RetailCloudCheckDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.retail.RetailEventLogService;
import com.trade_accounting.services.interfaces.units.CurrencyService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.retail.RetailCloudCheckService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.trade_accounting.config.SecurityConstants.RETAIL_RETAIL_CLOUD_CHECK_VIEW;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_RETAIL_CLOUD_CHECK_VIEW, layout = AppView.class)
@PageTitle("Очередь облачных чеков")*/
@SpringComponent
@UIScope
public class RetailCloudCheckView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailCloudCheckService retailCloudCheckService;
    private final CurrencyService currencyService;
    private final RetailStoreService retailStoreService;
    private final EmployeeService employeeService;
    private final RetailEventLogService retailEventLogService;
    private List<RetailCloudCheckDto> data;
    private final GridFilter<RetailCloudCheckDto> filter;
    private final Grid<RetailCloudCheckDto> grid = new Grid<>(RetailCloudCheckDto.class, false);
    private final GridConfigurer<RetailCloudCheckDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<RetailCloudCheckDto> paginator;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public RetailCloudCheckView(RetailCloudCheckService retailCloudCheckService, CurrencyService currencyService, RetailStoreService retailStoreService, EmployeeService employeeService, RetailEventLogService retailEventLogService) {
        this.retailCloudCheckService = retailCloudCheckService;
        this.data = getData();
        this.currencyService = currencyService;
        this.retailStoreService = retailStoreService;
        this.employeeService = employeeService;
        this.paginator = new GridPaginator<>(grid, data, 100);
        this.retailEventLogService = retailEventLogService;
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();

        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("retailStoreDto", RetailStoreDto::getName, retailStoreService.getAll());
        filter.setFieldToComboBox("fiscalPoint", RetailStoreDto::getName, retailStoreService.getAll());
        filter.setFieldToComboBox("cashierDto", EmployeeDto::getFirstName, employeeService.getAll());
        filter.setFieldToComboBox("currencyDto", CurrencyDto::getShortName, currencyService.getAll());

        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData2();
            paginator.setData(retailCloudCheckService.search(map));
        });
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("id");
        grid.addColumn("date").setHeader("Дата и время").setId("Дата и время");
        grid.addColumn(retailCloudCheckDto -> retailStoreService.getById(retailCloudCheckDto.getInitiatorId())
                .getName()).setKey("retailStoreDto").setHeader("Инициатор").setId("Инициатор");
        grid.addColumn(retailCloudCheckDto -> retailStoreService.getById(retailCloudCheckDto.getFiscalizationPointId())
                .getName()).setKey("fiscalPoint").setHeader("Точка фискализации").setId("Точка фискализации");
        grid.addColumn("status").setHeader("Статус").setId("Статус");
        grid.addColumn("cheskStatus").setHeader("Статус чека").setId("Статус чека");
        grid.addColumn("total").setTextAlign(ColumnTextAlign.END).setHeader("Итого").setId("Итого");

        grid.addColumn(retailCloudCheckDto -> currencyService.getById(retailCloudCheckDto.getCurrencyId())
                .getLetterCode()).setKey("currencyDto").setHeader("Валюта").setId("Валюта");

        grid.addColumn(retailCloudCheckDto -> employeeService.getById(retailCloudCheckDto.getCashierId())
                .getFirstName()).setKey("cashierDto").setHeader("Кассир").setId("Кассир");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        GridSortOrder<RetailCloudCheckDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private List<RetailCloudCheckDto> getData() {
        return retailCloudCheckService.getAll();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(),
                buttonCancel(), buttonEventLog(), textField(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Очередь облачных чеков");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
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
        GridPaginator<RetailCloudCheckDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailCloudCheckService.getAll(), 100);
        GridSortOrder<RetailCloudCheckDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private Button buttonCancel() {
        Button status = new Button("Отменить фискализацию");
        status.setWidth("220px");
        return status;
    }

    private Button buttonEventLog() {
        Button status = new Button("Журнал событий");
        EventLogModal eventLogModal = new EventLogModal(retailEventLogService);
        status.addClickListener(e -> eventLogModal.open());
        status.getStyle().set("cursor", "pointer");
        status.setWidth("170px");
        return status;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
