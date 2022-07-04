package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.company.PriceListProductService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.trade_accounting.services.interfaces.warehouse.ProductGroupService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS_PRICE_VIEW;
import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS__PRICE_LIST_EDIT;

@SpringComponent
@Route(value = GOODS_GOODS__PRICE_LIST_EDIT, layout = AppView.class)
@UIScope
@Slf4j
public class GoodsPriceLayoutPriceListView extends VerticalLayout implements AfterNavigationObserver {
    private PriceListDto priceListData;
    private Grid<PriceListProductDto> grid;
    private final VerticalLayout upperLayout;
    private final CompanyService companyService;
    private final PriceListService priceListService;
    private final ProductGroupService productGroupService;
    private final ProductService productService;
    private final PriceListProductPercentsService priceListProductPercentsService;
    private TextField listName = new TextField();
    private TextArea comments = new TextArea("Комментарии");
    private ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private DateTimePicker creationDate = new DateTimePicker();
    private List<PriceListProductDto> tempPriceListProducts = new ArrayList<>();
    private final GridPaginator<PriceListProductDto> paginator;
    private final PriceProductSelectModal productSelectModal;
    private final Binder<PriceListDto> priceListDtoBinder = new Binder<>(PriceListDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final PriceListProductService priceListProductService;
    private Checkbox isSpend = new Checkbox("Проведено", false);
    private final Notifications notifications;
    private Button filterButton = new Button("Фильтр");
    private final Dialog cancelEditingView = new Dialog();
    private PriceListProductPercentsDto priceListProductPercentsDto = new PriceListProductPercentsDto();

    public GoodsPriceLayoutPriceListView(CompanyService companyService, PriceListService priceListService,
                                         ProductService productService,
                                         ProductGroupService productGroupService,
                                         PriceListProductPercentsService priceListProductPercentsService, PriceProductSelectModal productSelectModal,
                                         PriceListProductService priceListProductService,
                                         Notifications notifications) {
        this.companyService = companyService;
        this.priceListService = priceListService;
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.productSelectModal = productSelectModal;
        this.priceListProductService = priceListProductService;
        this.notifications = notifications;
        this.upperLayout = new VerticalLayout();
        this.grid = new Grid<>(PriceListProductDto.class, false);
        paginator = new GridPaginator<>(grid, tempPriceListProducts, 50);
        configureUpperLayout();
        configureCloseViewDialog();
        comments.setWidth("1000px");
        comments.setHeightFull();
        add(upperLayout, grid, comments);

        this.productSelectModal.addDetachListener(detachEvent -> {
            if (productSelectModal.isFormValid()) {
                addProduct(productSelectModal.getPriceListProductDto());
            }
            productSelectModal.clearForm();
        });

    }

    private Component checkIsSpend() {
        isSpend.addValueChangeListener(event -> priceListData.setIsSpend(event.getValue().equals(true)));
        return isSpend;
    }

    public void addProduct(PriceListProductDto priceListProductDto) {
        if (!isProductInList(priceListProductDto)) {
            tempPriceListProducts.add(priceListProductDto);
            paginator.setData(tempPriceListProducts);

        } else {
            for (PriceListProductDto priceListProduct : tempPriceListProducts) {
                if (priceListProduct.getId().equals(priceListProductDto.getId())) {
                    tempPriceListProducts.remove(priceListProduct);
                    tempPriceListProducts.add(priceListProductDto);
                    break;
                }
            }
            paginator.setData(tempPriceListProducts);
        }
    }

    private void addPriceListProductToPriceListDto(PriceListDto priceList) {
        for (PriceListProductDto priceListProductDto : tempPriceListProducts) {
            priceListProductDto.setPriceListId(priceList.getId());
            priceListProductDto.setProductId(priceListProductDto.getProductId());
            priceListProductDto.setPrice(priceListProductDto.getPrice());
            priceListProductService.create(priceListProductDto);
        }
    }

    private boolean isProductInList(PriceListProductDto priceListProductDto) {
        return tempPriceListProducts.stream()
                .anyMatch(priceListProduct -> priceListProduct.getProductId()
                        .equals(priceListProductDto.getProductId()));
    }

    private void configureUpperLayout() {
        HorizontalLayout line1 = new HorizontalLayout();
        line1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        line1.add(getSaveButton(),
                getCloseButton(),
                getPriceListChanger(),
                getActionsMenuBar(),
                checkIsSpend());

        HorizontalLayout line2 = new HorizontalLayout();
        line2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label label1 = new Label("Прайс-лист №");
        Label label2 = new Label("от");
        MenuBar status = new MenuBar();
        MenuItem menuItem1 = status.addItem("Статус");
        SubMenu menuItem1SubMenu = menuItem1.getSubMenu();
        menuItem1SubMenu.addItem("Настроить");
        Button questionButton = Buttons.buttonQuestion("Непроведенный документ — это черновик. Он не учитывается в отчетах.");
        listName.setWidth("300px");
        line2.add(label1, listName, label2, creationDate, status, questionButton, isSpend);

        HorizontalLayout line3 = new HorizontalLayout();
        line3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout lineCompany = new HorizontalLayout();
        lineCompany.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label companyLabel = new Label("Организация");
        companyLabel.setWidth("105px");
        companyComboBox.setItems(companyService.getAll());
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("300px");
        lineCompany.add(companyLabel, companyComboBox);

        TextField filterCriteria = new TextField();
        line3.add(addPositionButton(), filterButton, filterCriteria);
        priceListDtoBinder.forField(listName).asRequired(TEXT_FOR_REQUEST_FIELD).bind("number");
        priceListDtoBinder.forField(creationDate).asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(PriceListDto::getTimeValid, PriceListDto::setTimeValid);
        priceListDtoBinder.forField(companyComboBox).asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(PriceListDto::getCompanyDtoValid, PriceListDto::setCompanyDtoValid);

        upperLayout.removeAll();
        upperLayout.add(line1, line2, lineCompany, line3);
    }

    private Button addPositionButton() {
        Button addPositionButton = new Button("Добавить позицию", new Icon(VaadinIcon.PLUS));
        addPositionButton.addClickListener(event -> {
            productSelectModal.setPriceList(priceListData, priceListProductPercentsDto);
            productSelectModal.open();
        });
        return addPositionButton;
    }

    private void deleteProduct(Long id) {
        PriceListProductDto found = new PriceListProductDto();
        for (PriceListProductDto priceListProductDto : tempPriceListProducts) {
            if (priceListProductDto.getProductId().equals(id)) {
                found = priceListProductDto;
                break;
            }
        }
        tempPriceListProducts.remove(found);
        paginator.setData(tempPriceListProducts);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(item -> productService.getById(item.getProductId()).getId()).setKey("productID").setHeader("Код").setSortable(true).setId("Код");
        grid.addColumn(item -> "").setKey("vendorCode").setHeader("Артикул").setId("Артикул");
        grid.addColumn(item -> productService.getById(item.getProductId()).getName()).setKey("productName").setHeader("Наимнование").setId("Наимнование");
        grid.addColumn(productDto -> (productGroupService.getById(productService.getById(productDto.getProductId())
                        .getProductGroupId())).getName()).setKey("groupName")
                .setHeader("Группа").setId("Группа");
        grid.addColumn(PriceListProductDto::getPrice).setKey("list2").setHeader("Цена").setId("Лист2");
        grid.setHeight("50vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemClickListener(event -> {
            PriceListProductDto priceListProductDto = event.getItem();
            productSelectModal.setProductSelect(priceListProductDto, priceListData, priceListProductPercentsDto);
            productSelectModal.open();
        });
        grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.TRASH));
            edit.addClassName("delete");
            edit.addClickListener(e -> deleteProduct(column.getProductId()));
            return edit;
        });
    }

    private Button getSaveButton() {
        return new Button("Сохранить", e -> {
            priceListDtoBinder.setValidatorsDisabled(false);

            if (!priceListDtoBinder.validate().isOk()) {
                priceListDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {

                for (PriceListProductDto priceListProductDto : priceListProductService.getByPriceListId(priceListData.getId())) {
                    if (!tempPriceListProducts.contains(priceListProductDto)) {
                        priceListProductService.deleteById(priceListProductDto.getId());
                    }
                }
                priceListData.setNumber(listName.getValue());
                priceListData.setTime(creationDate.getValue().toString());
                priceListData.setProductsIds(tempPriceListProducts.stream()
                        .map(PriceListProductDto::getProductId).collect(Collectors.toList()));
                addPriceListProductToPriceListDto(priceListData);
                priceListData.setCompanyId(companyComboBox.getValue().getId());
                if (priceListData.getPercentsIds() == null) {
                    priceListData.setPercentsIds(List.of(priceListProductPercentsDto.getId()));
                }
                priceListData.setCommentary(comments.getValue());
                priceListData.setIsSpend(isSpend.getValue());
                priceListService.update(priceListData);
                notifications.infoNotification(String.format("Заказ № %s сохранен", priceListData.getId()));
                priceListDtoBinder.setValidatorsDisabled(true);
                comments.clear();
            }
        });
    }

    private Button getCloseButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(buttonClickEvent -> cancelEditingView.open());
        return button;
    }

    private void configureCloseViewDialog() {
        cancelEditingView.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));
        cancelEditingView.setCloseOnEsc(false);
        cancelEditingView.setCloseOnOutsideClick(false);
        priceListDtoBinder.setValidatorsDisabled(false);

        Button confirmButton = new Button("Продолжить", event -> {
            comments.clear();
            cancelEditingView.close();
            paginator.reloadGrid();
            UI.getCurrent().navigate(GOODS_GOODS_PRICE_VIEW);
            priceListDtoBinder.setValidatorsDisabled(true);

        });
        Button cancelButton = new Button("Отменить", event -> {
            cancelEditingView.close();
            priceListDtoBinder.setValidatorsDisabled(true);
        });
        Shortcuts.addShortcutListener(cancelEditingView, cancelEditingView::close, Key.ESCAPE);
        cancelEditingView.add(new Div(confirmButton, new Div(), cancelButton));
    }

    private MenuBar getPriceListChanger() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
        menuBar.addItem(new Text("x из y"));
        menuBar.addItem(new Button(new Icon(VaadinIcon.CARET_LEFT)));
        menuBar.addItem(new Button(new Icon(VaadinIcon.CARET_RIGHT)));
        return menuBar;
    }

    private MenuBar getActionsMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

        MenuItem actions = menuBar.addItem("Действия");
        SubMenu actionsSubMenu = actions.getSubMenu();
        actionsSubMenu.addItem("Дополнить из остатков");
        actionsSubMenu.addItem("Дополнить из номенклатуры");

        MenuItem change = menuBar.addItem("Изменить");
        SubMenu changeSubmenu = change.getSubMenu();
        changeSubmenu.addItem("Удалить");
        changeSubmenu.addItem("Копировать");

        MenuItem print = menuBar.addItem(new Icon(VaadinIcon.PRINT));
        print.add("Печать");
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.addItem("Ценники");

        MenuItem send = menuBar.addItem(new Icon(VaadinIcon.ENVELOPE));
        send.add("Отправить");
        SubMenu sendSubMenu = send.getSubMenu();
        sendSubMenu.addItem("Ценники");
        sendSubMenu.addItem("Прайс-лист");

        return menuBar;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        this.grid = new Grid<>(PriceListProductDto.class, false);
    }

    public void setPriceListForCreate(PriceListDto priceListDto, PriceListProductPercentsDto priceListProductPercentsDto) {
        tempPriceListProducts.clear();
        this.priceListData = priceListDto;
        listName.setValue(priceListData.getNumber());
        creationDate.setValue(LocalDateTime.parse(priceListData.getTime()));
        companyComboBox.setValue(companyService.getById(priceListData.getCompanyId()));
        paginator.setData(tempPriceListProducts);
        this.priceListProductPercentsDto = priceListProductPercentsDto;
        configureGrid();
    }

    public void setPriceListForEdit(PriceListDto priceListDto, PriceListProductPercentsDto priceListProductPercentsDto) {
        this.priceListData = priceListDto;
        listName.setValue(priceListData.getNumber());
        creationDate.setValue(LocalDateTime.parse(priceListData.getTime()));
        companyComboBox.setValue(companyService.getById(priceListData.getCompanyId()));
        if (priceListData.getCommentary() != null) {
            comments.setValue(priceListData.getCommentary());
            isSpend.setValue(priceListData.getIsSpend());
        }
        tempPriceListProducts = priceListProductService.getByPriceListId(priceListData.getId());
        paginator.setData(tempPriceListProducts);
        this.priceListProductPercentsDto = priceListProductPercentsDto;
        configureGrid();
    }
}
