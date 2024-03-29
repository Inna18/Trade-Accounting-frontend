package com.trade_accounting.components.profile;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.util.ImageDto;
import com.trade_accounting.models.dto.client.PositionDto;
import com.trade_accounting.models.dto.client.RoleDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.util.ImageService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.client.RoleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AddEmployeeModalWindowView extends Dialog {

    private Long id;

    private final ValidTextField firstNameAdd = new ValidTextField(true, "Имя");

    private final ValidTextField middleNameAdd = new ValidTextField();

    private final ValidTextField lastNameAdd = new ValidTextField(true, "Фамилия");

    private final ValidTextField phoneAdd = new ValidTextField(true, "Телефон");

    private final ValidTextField emailAdd = new ValidTextField(true, "Адрес электронной почты");

    private final TextArea descriptionAdd = new TextArea();

    private final ValidTextField innAdd = new ValidTextField(true, "ИНН");

    private final PasswordField passwordAdd = new PasswordField();

    private final Select<RoleDto> rolesSelect = new Select<>();

    private final Select<DepartmentDto> departmentDtoSelect = new Select<>();

    private final Select<PositionDto> positionDtoSelect = new Select<>();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final Div div;

    transient private final EmployeeService employeeService;

    transient private final RoleService roleService;

    transient private final ImageService imageService;

    transient private final DepartmentService departmentService;

    transient private final PositionService positionService;

    transient private final Notifications notifications;

    transient private EmployeeDto employeeDto;

    transient private ImageDto imageDto;

    transient private Set<RoleDto> roles;

    transient private DepartmentDto departmentDto;

    transient private PositionDto positionDto;

    private Component avatarContainer;

    private Image avatar = new Image();

    private String header;

    @Autowired
    public AddEmployeeModalWindowView(EmployeeDto employeeDto,
                                      EmployeeService employeeService,
                                      RoleService roleService,
                                      ImageService imageService,
                                      ImageDto imageDto, Notifications notifications,
                                      DepartmentService departmentService,
                                      PositionService positionService,
                                      String title) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.imageService = imageService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.notifications = notifications;
        this.header = title;
        div = new Div();
        if (employeeDto != null) {
            this.employeeDto = employeeDto;
            id = employeeDto.getId();
            firstNameAdd.setValue(getFieldValueNotNull(employeeDto.getFirstName()));
            middleNameAdd.setValue(getFieldValueNotNull(employeeDto.getMiddleName()));
            lastNameAdd.setValue(getFieldValueNotNull(employeeDto.getLastName()));
            phoneAdd.setValue(getFieldValueNotNull(employeeDto.getPhone()));
            emailAdd.setValue(getFieldValueNotNull(employeeDto.getEmail()));
            innAdd.setValue(getFieldValueNotNull(employeeDto.getInn()));
            descriptionAdd.setValue(getFieldValueNotNull(employeeDto.getDescription()));
            passwordAdd.setValue(getFieldValueNotNull(employeeDto.getPassword()));
            departmentDto = departmentService.getById(employeeDto.getDepartmentDtoId());
            positionDto = positionService.getById(employeeDto.getPositionDtoId());
            roles = employeeDto.getRoleDtoIds().stream().map(map -> roleService.getById(map)).collect(Collectors.toSet());
        }
        this.imageDto = imageDto;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(title(title), header());
        add(upperLayout(), lowerLayout());
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(addButtonSave(), getCancelButton(), getImageButton());
        return header;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout lowerLayout = new HorizontalLayout();
        div.removeAll();

        div.add(addEmployeeLastName(),
                addEmployeeFirstName(),
                addEmployeeMiddleName(),
                addEmployeeEmail(),
                addEmployeePhone(),
                addEmployeeInn(),
                addEmployeeDescription(),
                addEmployeePassword(),
                rolesSelect(),
                departmentSelect(),
                positionSelect(),
                addEmployeeImage()
        );
        add(div);
        return lowerLayout;
    }

    private HorizontalLayout positionSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<PositionDto> positionDtoList = positionService.getAll();

        positionDtoSelect.setItemLabelGenerator(PositionDto::getName);
        positionDtoSelect.setItems(positionDtoList);

        if (positionDto != null) {
            positionDtoSelect.setValue(positionService.getById(employeeDto.getDepartmentDtoId()));
        }

        positionDtoSelect.setWidth(fieldWidth);
        positionDtoSelect.setPlaceholder("Введите позицию");
        Label label = new Label("Позиция");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, positionDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout departmentSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<DepartmentDto> departmentDtoList = departmentService.getAll();

        departmentDtoSelect.setItemLabelGenerator(DepartmentDto::getName);
        departmentDtoSelect.setItems(departmentDtoList);

        if (departmentDto != null) {
           departmentDtoSelect.setValue(departmentService.getById(employeeDto.getDepartmentDtoId()));
        }

        departmentDtoSelect.setWidth(fieldWidth);
        departmentDtoSelect.setPlaceholder("Введите отдел");
        Label label = new Label("Отдел");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, departmentDtoSelect);
        return horizontalLayout;
    }

    private Component addEmployeeImage() {
        Label label = new Label("Фото профиля");
        label.setWidth(labelWidth);
        if (imageDto != null && imageDto.getContent() != null) {
            avatar = this.setImage(imageDto.getContent());
        }
        avatar.setHeight("200px");
        if (avatarContainer != null) {
            div.remove(avatarContainer);
        }
        return avatarContainer = new HorizontalLayout(label, avatar);
    }

    private Component addEmployeePassword() {
        Label label = new Label("Пароль");
        label.setWidth(labelWidth);
        passwordAdd.setWidth(fieldWidth);
        passwordAdd.setPlaceholder("Введите пароль");
        passwordAdd.setRequired(true);
        HorizontalLayout addEmployeePasswordAddLayout = new HorizontalLayout(label, passwordAdd);
        return addEmployeePasswordAddLayout;
    }

    private Component addEmployeeEmail() {
        Label label = new Label("Email");
        label.setWidth(labelWidth);
        emailAdd.setWidth(fieldWidth);
        emailAdd.setPlaceholder("Введите Email");
        emailAdd.addValidator(new EmailValidator("Введите правильно адрес электронной почты!"));
        emailAdd.setRequired(true);
        if (employeeDto != null) {
            emailAdd.setValue(employeeDto.getEmail());
        }
        HorizontalLayout addEmployeeEmailAddLayout = new HorizontalLayout(label, emailAdd);
        return addEmployeeEmailAddLayout;
    }

    private Component addEmployeeDescription() {
        Label label = new Label("Описание");
        label.setWidth(labelWidth);
        descriptionAdd.setWidth(fieldWidth);
        descriptionAdd.setPlaceholder("Введите описания сотрудника");
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, descriptionAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeePhone() {
        Label label = new Label("Телефон");
        label.setWidth(labelWidth);
        phoneAdd.setWidth(fieldWidth);
        phoneAdd.setPlaceholder("Введите номер телефона");
        phoneAdd.addValidator(new RegexpValidator("Введите правильно номер телефона", "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"));
        phoneAdd.setRequired(true);
        if (employeeDto != null) {
            phoneAdd.setValue(employeeDto.getPhone());
        }
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, phoneAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeInn() {
        Label label = new Label("ИНН");
        label.setWidth(labelWidth);
        innAdd.setWidth(fieldWidth);
        innAdd.setPlaceholder("Введите ИНН");
        innAdd.setPattern("^[\\d+]{12}$");
        innAdd.addValidator(new RegexpValidator("Введите правильно ИНН", "^[\\d+]{12}$"));
        innAdd.setRequired(true);
        if (employeeDto != null) {
            innAdd.setValue(employeeDto.getInn());
        }
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, innAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeLastName() {
        Label label = new Label("Фамилия");
        label.setWidth(labelWidth);
        lastNameAdd.addValidator(new StringLengthValidator("Введите правильно фамилию", 1, 50));
        lastNameAdd.setWidth(fieldWidth);
        lastNameAdd.setPlaceholder("Введите фамилию");
        lastNameAdd.setRequired(true);
        if (employeeDto != null) {
            lastNameAdd.setValue(employeeDto.getLastName());
        }
        HorizontalLayout lastNameLayout = new HorizontalLayout(label, lastNameAdd);
        return lastNameLayout;
    }

    private Component addEmployeeFirstName() {
        Label label = new Label("Имя");
        label.setWidth(labelWidth);
        firstNameAdd.setWidth(fieldWidth);
        firstNameAdd.setPlaceholder("Введите Имя");
        firstNameAdd.setRequired(true);
        firstNameAdd.addValidator(new StringLengthValidator("Введите правильно имя", 1, 50));
        if (employeeDto != null) {
            firstNameAdd.setValue(employeeDto.getFirstName());
        }
        HorizontalLayout firstNameLayout = new HorizontalLayout(label, firstNameAdd);
        return firstNameLayout;
    }

    private Component addEmployeeMiddleName() {
        Label label = new Label("Отчество");
        label.setWidth(labelWidth);
        middleNameAdd.setWidth(fieldWidth);
        middleNameAdd.setPlaceholder("Введите Отчество");
        HorizontalLayout middleNameLayout = new HorizontalLayout(label, middleNameAdd);
        return middleNameLayout;
    }

    private HorizontalLayout rolesSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<RoleDto> rolesDto = roleService.getAll();

        rolesSelect.setItemLabelGenerator(RoleDto::getName);
        rolesSelect.setItems(rolesDto);
        if (roles != null) {
            for (RoleDto role : roles)
                rolesSelect.setValue(role);
        }
        rolesSelect.setWidth(fieldWidth);
        Label label = new Label("Роль");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, rolesSelect);
        return horizontalLayout;
    }

    private Component addButtonSave() {
        Button buttonSave = new Button("Сохранить");
        buttonSave.addClickListener(click -> {
            if (id == null) {
                log.info("Вы нажали кнопку для сохранения нового сотрудника!");
                notifications.infoNotification("Вы добавили нового сотрудника");

                if (!lastNameAdd.isRequiredVerify() || !firstNameAdd.isRequiredVerify() || !emailAdd.isRequiredVerify()
                        || !phoneAdd.isRequiredVerify() || !innAdd.isRequiredVerify()) {
                    return;
                }

                if (emailAdd.isInvalid() || innAdd.isInvalid() || phoneAdd.isInvalid() || lastNameAdd.isInvalid() || firstNameAdd.isInvalid()) {
                    log.info("ошибка в введенных данных на форме!");
                    notifications.errorNotification("Ошибка в введенных данных на форме");
                } else {
                    EmployeeDto newEmployeeDto = setEmployeeDto(null);
                    employeeService.create(newEmployeeDto);
                    div.removeAll();
                    close();
                }
            } else {
                log.info("Вы нажали кнопку для обновления сотрудника!");
                notifications.infoNotification("Вы отредактировали сотрудника");
                EmployeeDto updateEmployeeDto = setEmployeeDto(id);
                employeeService.update(updateEmployeeDto);
                div.removeAll();
                close();
            }
        });
        return buttonSave;
    }

    private EmployeeDto setEmployeeDto(Long id) {
        EmployeeDto updateEmployeeDto = new EmployeeDto();
        if (id != null) {
            updateEmployeeDto.setId(id);
        }
        updateEmployeeDto.setFirstName(firstNameAdd.getValue());
        updateEmployeeDto.setLastName(lastNameAdd.getValue());
        updateEmployeeDto.setMiddleName(middleNameAdd.getValue());
        updateEmployeeDto.setEmail(emailAdd.getValue());
        updateEmployeeDto.setInn(innAdd.getValue());
        updateEmployeeDto.setPhone(phoneAdd.getValue());
        updateEmployeeDto.setDescription(descriptionAdd.getValue());
        updateEmployeeDto.setPassword(passwordAdd.getValue());
        updateEmployeeDto.setRoleDtoIds(getRoles().stream().map(RoleDto::getId).collect(Collectors.toSet()));
        updateEmployeeDto.setImageDtoId(imageDto.getId());
        updateEmployeeDto.setDepartmentDtoId(departmentDtoSelect.getValue().getId());
        updateEmployeeDto.setPositionDtoId(positionDtoSelect.getValue().getId());
        return updateEmployeeDto;
    }

    private Set<RoleDto> getRoles() {
        Set<RoleDto> roleDtos = new HashSet<>();
        roleDtos.add(rolesSelect.getValue());
        return roleDtos;
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }

    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        upload.addFinishedListener(event -> {
            byte[] content = new byte[0];
            try {
                content = memoryBuffer.getInputStream().readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            avatar = this.setImage(content);
            imageDto = new ImageDto();
            final String fileName = event.getFileName();
            String fileExtension = fileName.substring(fileName.indexOf("."));
            imageDto.setFileExtension(fileExtension);
            imageDto.setContent(content);
            imageDto.setId(ZonedDateTime.now().toInstant().toEpochMilli());
            imageService.create(imageDto);
            div.add(avatarContainer = this.addEmployeeImage());
            dialog.close();
        });

        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
    }

    private H2 title(String text) {
        H2 title = new H2(text);
        title.setHeight("2.2em");
        return title;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

    private Image setImage(byte[] content) {
        return new Image(new StreamResource("image", () -> new ByteArrayInputStream(content)), "");
    }
}