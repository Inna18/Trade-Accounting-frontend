package com.trade_accounting.components.tasks;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@SpringComponent
@UIScope
@Slf4j
@Route(value = "tasks", layout = AppView.class)
@PageTitle("Задачи")
public class TasksView extends VerticalLayout {

    private final TaskService taskService;
    private final Grid<TaskDto> grid = new Grid<>(TaskDto.class, false);
    private final GridFilter<TaskDto> filter;
    private final EmployeeService employeeService;
    private final ContractorService contractorService;
    private final GridPaginator<TaskDto> paginator;

    private final TaskDto taskDto;

    @Autowired
    public TasksView(TaskService taskService, EmployeeService employeeService, ContractorService contractorService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.contractorService = contractorService;
        this.taskDto = new TaskDto();
        paginator = new GridPaginator<>(grid, taskService.getAll(), 15);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolBar(), filter, grid, paginator);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn("description").setHeader("Описание").setId("Описание");
        grid.addColumn(e -> contractorService.getById(e.getContractorId()).getName())
                .setKey("contractorId").setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(e -> employeeService.getById(e.getEmployeeId()).getLastName())
                .setKey("employeeId").setHeader("Ответственный").setId("Ответственный");
        grid.addColumn(TaskDto::getDeadlineDateTime).setHeader("Срок")
                .setKey("deadLineDateTime").setId("Срок");
        grid.addColumn(TaskDto::getCreationDateTime).setHeader("Создано")
                .setKey("creationDateTime").setId("Создано");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            TaskDto taskDto = event.getItem();
            TaskModalWin addTaskModalWin =
                    new TaskModalWin(taskService, taskDto, employeeService, contractorService);
            addTaskModalWin.addDetachListener(e -> updateList());
            addTaskModalWin.getSaveButton();
            addTaskModalWin.open();
        });
    }

    private HorizontalLayout getToolBar() {
        var toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextTask(), getButtonRefresh(),
                getButtonCreateTask(), getButtonFilter(), getTextField());

        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return toolbar;
    }

    private Button getButtonQuestion() {
        var buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        String notificationText = "Задачи помогают организовать работу. Их можно" +
                " ставить себе или другим сотрудникам, выполнение отслеживается по уведомлениям.\n" +
                "\nЗадачу можно создать из любого документа. Также можно настроить" +
                " автоматическое создание задач в рамках сценариев. Например, если" +
                " покупатель в течение недели не оплачивает счет, можно поставить " +
                "менеджеру задачу связаться с ним.\n\nЧитать инструкцию: Задачи";

        var notification = new Notification(
                notificationText, 3000, Notification.Position.BOTTOM_START);

        buttonQuestion.addClickListener(event -> notification.open());

        return buttonQuestion;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator.setData(taskService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(taskService.getAll()));
    }

    private H2 getTextTask() {
        final var textTask = new H2("Задачи");
        textTask.setHeight("2.2em");
        return textTask;
    }

    private Button getButtonRefresh() {
        final var buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button getButtonCreateTask() {
        var buttonUnit = new Button("Задача", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(click -> {
            TaskModalWin taskModalWin = new TaskModalWin(taskService, taskDto, employeeService, contractorService);
            taskModalWin.open();
        });
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private TextField getTextField() {
        var textField = new TextField();
        textField.setPlaceholder("Задача, комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");

        return textField;
    }

    private void updateList() {
        GridPaginator<TaskDto> paginatorUpdateList
                = new GridPaginator<>(grid, taskService.getAll(), 15);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolBar(), filter, grid, paginator);
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date).format(DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private List<TaskDto> getData() {
        return taskService.getAll();
    }
}
