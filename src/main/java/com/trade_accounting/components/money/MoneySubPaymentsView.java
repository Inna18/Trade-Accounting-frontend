package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.PaymentDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.MONEY_MONEY_PAYMENTS_VIEW;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = MONEY_MONEY_PAYMENTS_VIEW, layout = AppView.class)
@PageTitle("Платежи")*/
@CssImport(value = "styles/conducted_grid_style.css", themeFor = "vaadin-grid")
public class MoneySubPaymentsView extends VerticalLayout {
    private final transient PaymentService paymentService;
    private final transient CompanyService companyService;
    private final transient ContractorService contractorService;
    private final transient ProjectService projectService;
    private final transient ContractService contractService;
    private final transient Notifications notifications;

    private final transient List<PaymentDto> data;
    private final Grid<PaymentDto> grid = new Grid<>(PaymentDto.class, false);
    private final GridConfigurer<PaymentDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<PaymentDto> paginator;
    private final GridFilter<PaymentDto> filter;
    private final CreditOrderModal creditOrderModal;
    private final IncomingPaymentModal incomingPaymentModal;
    private final OutgoingPaymentModal outgoingPaymentModal;
    private final ExpenseOrderModal expenseOrderModal;
    private final Scroller scroller = new Scroller();
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    MoneySubPaymentsView(PaymentService paymentService,
                         CompanyService companyService,
                         ContractorService contractorService,
                         ProjectService projectService,
                         ContractService contractService,
                         Notifications notifications,
                         CreditOrderModal creditOrderModal,
                         IncomingPaymentModal incomingPaymentModal,
                         ExpenseOrderModal expenseOrderModal,
                         OutgoingPaymentModal outgoingPaymentModal) {
        this.paymentService = paymentService;
        this.data = getDate();
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.creditOrderModal = creditOrderModal;
        this.incomingPaymentModal = incomingPaymentModal;
        this.expenseOrderModal = expenseOrderModal;
        this.outgoingPaymentModal = outgoingPaymentModal;


        configureGrid();
        configureScroller();
        this.paginator = new GridPaginator<>(grid, data, 10);
        this.filter = new GridFilter<>(grid);
        //configureScroller();
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<Div> list = getCreditAndExpense();
        horizontalLayout.add(paginator, list.get(0), list.get(1));
        add(getToolbar(), filter, grid, horizontalLayout);
    }

    private void configureScroller() {
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.setContent(grid);
        add(scroller);
    }

    private void configureFilter() {
        filter.setFieldToComboBox("typeOfDocument", "Входящий платеж", "Приходный ордер", "Исходящий платеж", "Расходный ордер");
        filter.setFieldToIntegerField("number");
        filter.setFieldToDatePicker("time");
        filter.setFieldToComboBox("paymentMethods", "CASH", "BANK");
        filter.setFieldToComboBox("expenseItem", "RETURN",
                "PURCHACE",
                "TAXESANDFEES",
                "MOVEMENT",
                "RENTAL",
                "SALARY",
                "MARKETING");
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
        filter.onSearchClick(e -> paginator.setData(paymentService.filter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(getDate()));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.setItems(data);
        grid.addColumn("typeOfDocument").setHeader("Тип документа")
                .setId("Тип документа");
        grid.addColumn("number").setHeader("№")
                .setId("№");
        grid.addColumn("time").setHeader("Дата и время")
                .setId("Дата и время");
        grid.addColumn(pDto -> companyService.getById(pDto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyDto")
                .setId("Компания");
        grid.addColumn(pDto -> contractorService.getById(pDto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto")
                .setId("Контрагент");
        grid.addColumn(paymentDto -> {
            if (paymentDto.getTypeOfPayment().equals("INCOMING")) {
                return paymentDto.getSum();
            } else {
                return "";
            }
        }).setHeader("Приход").setTextAlign(ColumnTextAlign.END).setKey("sum").setId("Приход");
        grid.addColumn(paymentDto -> {
            if (paymentDto.getTypeOfPayment().equals("OUTGOING")) {
                return paymentDto.getSum();
            } else {
                return "";
            }
        }).setHeader("Расход").setTextAlign(ColumnTextAlign.END).setKey("sumOut").setId("Расход");
        grid.addColumn("paymentMethods").setHeader("Способ оплаты").setId("Способ оплаты");
        grid.addColumn("expenseItem").setHeader("Статья расходов").setId("Статья расходов");
        grid.addColumn(pDto -> contractService.getById(pDto.getContractId()).getNumber()).setHeader("Договор")
                .setKey("contractDto").setId("Договор");
        grid.addColumn(pDto -> {
            Long projectId = pDto.getProjectId();
            return projectId != null && projectId != 0 ? projectService.getById(projectId).getName() : "";
        }).setHeader("Проект").setKey("projectDto").setId("Проект");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setClassNameGenerator(paymentDto -> {
            if (paymentDto.getConducted() == false) {
                return "not-conducted";
            }
            return "";
        });
        grid.addItemClickListener(event -> {
            PaymentDto editPaymentDto = event.getItem();
            switch (editPaymentDto.getTypeOfDocument()) {
                case "Входящий платеж":
                    IncomingPaymentModal incomingPaymentModal = new IncomingPaymentModal(
                            paymentService,
                            companyService,
                            contractorService,
                            projectService,
                            contractService,
                            notifications);
                    incomingPaymentModal.addDetachListener(e -> updateList());
                    incomingPaymentModal.setPaymentDataForEdit(editPaymentDto);
                    incomingPaymentModal.open();
                    break;
                case "Приходный ордер":
                    CreditOrderModal addCreditOrderModal = new CreditOrderModal(
                            paymentService,
                            companyService,
                            contractorService,
                            projectService,
                            contractService,
                            notifications);
                    addCreditOrderModal.addDetachListener(e -> updateList());
                    addCreditOrderModal.setPaymentDataForEdit(editPaymentDto);
                    addCreditOrderModal.open();
                    break;
                case "Исходящий платеж":
                    OutgoingPaymentModal outgoingPaymentModal = new OutgoingPaymentModal(
                            paymentService,
                            companyService,
                            contractorService,
                            projectService,
                            contractService,
                            notifications);
                    outgoingPaymentModal.addDetachListener(e -> updateList());
                    outgoingPaymentModal.setPaymentDataForEdit(editPaymentDto);
                    outgoingPaymentModal.open();
                    break;
                default:
                    ExpenseOrderModal expenseOrderModal = new ExpenseOrderModal(
                            paymentService,
                            companyService,
                            contractorService,
                            projectService,
                            contractService,
                            notifications);
                    expenseOrderModal.addDetachListener(e -> updateList());
                    expenseOrderModal.setPaymentDataForEdit(editPaymentDto);
                    expenseOrderModal.open();
                    break;
            }
        });
    }

    private void updateList() {
        GridPaginator<PaymentDto> paginatorUpdateList
                = new GridPaginator<>(grid, paymentService.getAll(), 10);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextPayments(), getButtonRefresh(), getComingMenuBar(), getConsumptionMenuBar(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getPrint(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonCog() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField getTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(paymentService.getAll());
        } else paginator.setData(paymentService.search(search));
    }

    /*Кнопка Фильтр*/
    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    /*Меню Приход*/
    private MenuBar getComingMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Приход");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Приходный ордер", menuItemClickEvent -> creditOrderModal.open());
        subMenu.addItem("Входящий платеж", menuItemClickEvent -> incomingPaymentModal.open());
        return menuBar;
    }

    /*Меню Расход*/
    private MenuBar getConsumptionMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Расход");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Расходный ордер", menuItemClickEvent -> expenseOrderModal.open());
        subMenu.addItem("Исходящий платеж", menuItemClickEvent -> outgoingPaymentModal.open());
        return menuBar;
    }

    /*Кнопка Обновления*/
    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    /*Заголовок*/
    private H2 getTextPayments() {
        final H2 textCompany = new H2("Платежи");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    /*Кнопка Вопрос*/
    private Button getButtonQuestion() {
        return Buttons.buttonQuestion("На основе платежей формируются взаиморасчеты с контрагентами.\n" +
                "\n" +
                "Входящий и исходящий платежи изменяют баланс на расчетных счетах организации, то есть используются " +
                "для учета безналичных платежей. Приходный и расходный ордеры учитывают наличные платежи, то есть изменяют " +
                "баланс в кассе организации. Также можно импортировать выписку из банка.\n" +
                "\n" +
                "Платежные документы можно привязать сразу к нескольким товарным документам.");
    }

    /*Меню Изменить*/
    private MenuBar getSelect() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Изменить");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Провести", menuItemClickEvent -> {
            conductPayments();
            grid.deselectAll();
            paginator.setData(getDate());
        });
        subMenu.addItem("Снять проведение", menuItemClickEvent -> {
            removeConductPayments();
            grid.deselectAll();
            paginator.setData(getDate());
        });
        subMenu.addItem("Удалить", menuItemClickEvent -> {
            deleteSelectedPayments();
            grid.deselectAll();
            paginator.setData(getDate());
        });
        return menuBar;
    }

    /*Удаление выбранных платежей*/
    private void deleteSelectedPayments() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PaymentDto paymentDto : grid.getSelectedItems()) {
                paymentService.deleteById(paymentDto.getId());
                notifications.infoNotification("Выбранные платежи успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные платежы");
        }
    }

    private void conductPayments() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PaymentDto paymentDto : grid.getSelectedItems()) {
                paymentService.getById(paymentDto.getId()).setConducted(true);
                notifications.infoNotification("Выбранные платежи успешно проведены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные платежы");
        }
    }

    private void removeConductPayments() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PaymentDto paymentDto : grid.getSelectedItems()) {
                paymentService.getById(paymentDto.getId()).setConducted(false);
                notifications.infoNotification("Выбранные платежи успешно распроведены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные платежы");
        }
    }

    /*Меню Печать*/
    private MenuBar getPrint() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Печать");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Список всех платежей", menuItemClickEvent -> {
            PaymentPrintModal paymentPrintModal = new PaymentPrintModal(companyService, contractService, contractorService, projectService, paymentService);
            paymentPrintModal.open();
        });
        return menuBar;
    }

    private List<PaymentDto> getDate() {
        return paymentService.getAll().stream().sorted(Comparator.comparing(PaymentDto::getId)).collect(Collectors.toList());
    }

    private List<Div> getCreditAndExpense() {
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (PaymentDto paymentDto : data) {
            if (paymentDto.getConducted()) {
                if (paymentDto.getTypeOfPayment().equals("INCOMING")) {
                    credit = credit.add(paymentDto.getSum());
                } else {
                    expense = expense.add(paymentDto.getSum());
                }
            }
        }
        Div divCredit = new Div();
        divCredit.setText("Приход: " + credit);
        Div divExpense = new Div();
        divExpense.setText("Расход: " + expense);

        List<Div> list = new ArrayList<>();
        list.add(divCredit);
        list.add(divExpense);
        return list;
    }
}
