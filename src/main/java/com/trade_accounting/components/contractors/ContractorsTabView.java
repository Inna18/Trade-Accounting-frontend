package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.services.interfaces.company.AddressService;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.ContactService;
import com.trade_accounting.services.interfaces.company.ContractorGroupService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.trade_accounting.services.interfaces.company.TypeOfContractorService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.interfaces.dadata.DadataAddressService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = CONTRACTORS_CONTRACTORS_VIEW, layout = AppView.class)
@PageTitle("Контрагенты")*/
public class ContractorsTabView extends VerticalLayout {


    private final Notifications notifications;
    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final TypeOfContractorService typeOfContractorService;
    private final TypeOfPriceService typeOfPriceService;
    private final LegalDetailService legalDetailService;
    private final ContractorStatusService contractorStatusService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;
    private final AddressService addressService;
    private final List<ContractorDto> data;
    private final Grid<ContractorDto> grid = new Grid<>(ContractorDto.class, false);
    private final GridConfigurer<ContractorDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<ContractorDto> paginator;
    private final GridFilter<ContractorDto> filter;
    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final MenuItem print;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/contractors_templates/";
    private final ContactService contactService;
    private final DadataAddressService dadataAddressService;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public ContractorsTabView(Notifications notifications,
                              ContractorService contractorService,
                              ContractorGroupService contractorGroupService,
                              TypeOfContractorService typeOfContractorService,
                              TypeOfPriceService typeOfPriceService,
                              LegalDetailService legalDetailService, ContractorStatusService contractorStatusService,
                              DepartmentService departmentService, EmployeeService employeeService,
                              BankAccountService bankAccountService, AddressService addressService,
                              ContactService contactService, DadataAddressService dadataAddressService) {
        this.notifications = notifications;
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        this.typeOfContractorService = typeOfContractorService;
        this.typeOfPriceService = typeOfPriceService;
        this.legalDetailService = legalDetailService;
        this.contractorStatusService = contractorStatusService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.bankAccountService = bankAccountService;
        this.addressService = addressService;
        this.contactService = contactService;
        this.dadataAddressService = dadataAddressService;
        print = selectXlsTemplateButton.addItem("печать");

        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();

        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
        configureSelectXlsTemplateButton();
        updateList();
    }


    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn("shortname").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.addColumn("sortNumber").setHeader("Номер").setId("Номер");
        grid.addColumn("phone").setHeader("Телефон").setId("Телефон");
        grid.addColumn("fax").setHeader("Факс").setId("Факс");
        grid.addColumn("email").setHeader("E-mail").setId("E-mail");
        grid.addColumn(iDto -> addressService.getById(iDto.getAddressId()).toString()).setHeader("Адрес").setKey("address")
                .setId("Адрес");
        grid.addColumn(ContractorDto::getCommentToAddress).setHeader("Комментарий к адресу").setKey("commentToAddress")
                .setId("Комментарий к адресу");
        grid.addColumn(ContractorDto::getComment).setHeader("Комментарий").setKey("comment").setId("Комментарий");
        grid.addColumn(iDto -> contractorGroupService.getById(iDto.getContractorGroupId()).getName()).setHeader("Группы")
                .setKey("contractorGroupDto").setId("Группы");
        grid.addColumn(iDto -> typeOfPriceService.getById(iDto.getTypeOfPriceId()).getName()).setHeader("Скидки и цены").setKey("typeOfPriceDto").setId("Скидки и цены");
        grid.addColumn(iDto -> legalDetailService.getById(iDto.getLegalDetailId()).getInn()).setHeader("Реквизиты").setKey("legalDetailDto").setId("Реквизиты");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            ContractorDto contractorDto = event.getItem();
            ContractorModalWindow contractorModalWindow = new ContractorModalWindow(
                    contractorDto,
                    contractorService,
                    contractorGroupService,
                    typeOfContractorService,
                    typeOfPriceService,
                    legalDetailService,
                    contractorStatusService,
                    departmentService,
                    employeeService,
                    bankAccountService,
                    addressService,
                    contactService,
                    dadataAddressService
                    );
            contractorModalWindow.setContractorDataForEdit(contractorDto);
            contractorModalWindow.open();
        });
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Контрагент", new Icon(VaadinIcon.PLUS_CIRCLE));
        ContractorModalWindow addContractorModalWindowCreate =
                new ContractorModalWindow(new ContractorDto(),
                        contractorService, contractorGroupService, typeOfContractorService, typeOfPriceService,
                        legalDetailService, contractorStatusService, departmentService, employeeService, bankAccountService, addressService,
                        contactService, dadataAddressService);
        buttonUnit.addClickListener(event -> addContractorModalWindowCreate.open());
        addContractorModalWindowCreate.addDetachListener(event -> updateList());
        return buttonUnit;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e ->
                paginator.setData(contractorService.searchContractor(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(contractorService.getAll()));
    }

    private List<ContractorDto> getData() {
        return contractorService.getAll();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(),
                buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings(), selectXlsTemplateButton);
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Наимен, ИНН, тел., коммент...");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(contractorService.searchByTerm(textField.getValue()));
        } else {
            grid.setItems(contractorService.searchByTerm("null"));
        }
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе представлен список ваших поставщиков и покупателей. " +
                "Для них можно настраивать индивидуальные цены и скидки, также " +
                "можно им звонить и отправлять документы прямо из МоегоСклада. " +
                "Список контрагентов можно импортировать и экспортировать.");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private H2 title() {
        H2 title = new H2("Контрагенты");
        title.setHeight("2.2em");
        return title;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
                    deleteSelectedContractors();
                    grid.deselectAll();
                }
        );
    }

    private void updateList() {
        GridPaginator<ContractorDto> paginatorUpdateList
                = new GridPaginator<>(grid, contractorService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginator);
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("добавить шаблон");
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog);
        dialog.add(upload);
        menuItem.addClickListener(x -> dialog.open());
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    configureSelectXlsTemplateButton();
                    getInfoNotification("Файл успешно загружен");
                    log.info("xls шаблон успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
    }

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToPdfTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToOdsTemplate(x)));
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        PrintContractorsXls printContractorsXls = new PrintContractorsXls(file.getPath(), contractorService.getAll(), legalDetailService, addressService);
        return new Anchor(new StreamResource(templateName, printContractorsXls::createReport), "Скачать в формате Excel");
    }

    private Anchor getLinkToPdfTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        PrintContractorsXls printContractorsXls = new PrintContractorsXls(file.getPath(), contractorService.getAll(), legalDetailService, addressService);
        return new Anchor(new StreamResource(templateName, printContractorsXls::createReportPDF), "Скачать в формате PDF");
    }

    private Anchor getLinkToOdsTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";
        PrintContractorsXls printContractorsXls = new PrintContractorsXls(file.getPath(), contractorService.getAll(), legalDetailService, addressService);
        return new Anchor(new StreamResource(templateName, printContractorsXls::createReportODS), "Скачать в формате Office Calc");
    }

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }

    private void deleteSelectedContractors() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ContractorDto contractorDto : grid.getSelectedItems()) {
                contractorService.deleteById(contractorDto.getId());
                notifications.infoNotification("Выбранные контрагенты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные контрагенты");
        }
    }

    private void getErrorNotification(String message) {
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(message);
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        String styles = ".my-style { color: red; }";
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }
}



