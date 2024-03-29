package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductPriceDto;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.stereotype.Component;

@SpringComponent
@UIScope
@Component
public class SalesChooseGoodsModalWin extends Dialog {

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";

    private final ProductService productService;
    private final ProductPriceService productPriceService;


    private List<ProductDto> productDtos;

    public final ComboBox<ProductDto> productSelect = new ComboBox<>();
    public final ComboBox<ProductPriceDto> priceSelect = new ComboBox<>();
    public final BigDecimalField amountField = new BigDecimalField();

    public Button saveButton = new Button();

    public SalesChooseGoodsModalWin(ProductService productService, ProductPriceService productPriceService) {
        this.productService = productService;
        this.productPriceService = productPriceService;
        add(header(), configureProductSelect(), configurePriceSelect(), configureAmountField());
    }

    private HorizontalLayout header() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(getSaveButton(), getCloseButton()
        );
        return headerLayout;
    }

    public void updateProductList() {
        productSelect.setItems(productService.getAll());
    }

    public void updatePricesList() {
        if (productSelect.getValue() != null){
            priceSelect.setItems(productPriceService.getAll()
                    .stream()
                    .filter(x -> productSelect.getValue().getProductPriceIds().contains(x.getId()))
                    .collect(Collectors.toList())
            );
            priceSelect.setItemLabelGenerator(x -> x.getValue().toString());
            priceSelect.addValueChangeListener(e -> {
                updateSaveButtonEnable();
            });
        }
    }

    private HorizontalLayout configureProductSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        productDtos = productService.getAll();
        if (productDtos != null) {
            productSelect.setItems(productDtos);
        }
        productSelect.setItemLabelGenerator(ProductDto::getName);
        productSelect.setWidth(FIELD_WIDTH);
        Label label = new Label("Выберите продукт");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, productSelect);

        productSelect.addValueChangeListener(e -> {
            updatePricesList();
            updateSaveButtonEnable();
        });

        return horizontalLayout;
    }

    private HorizontalLayout configurePriceSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Выберите цену");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, priceSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureAmountField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        amountField.setValue(BigDecimal.ONE);
        amountField.setErrorMessage("Требуется положительное число");
        Label label = new Label("Укажите количество");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, amountField);

        amountField.addValueChangeListener(e -> {
            amountField.setInvalid(!isAmountFieldValid());
            updateSaveButtonEnable();
        });

        return horizontalLayout;
    }

    private boolean isAmountFieldValid() {
        return amountField.getValue() != null && amountField.getValue().compareTo(BigDecimal.ZERO) > 0;
    }

    private Button getSaveButton() {
        saveButton = new Button("Добавить", event -> close());
        saveButton.setEnabled(false);
        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> close());
    }

    public InvoiceProductDto getInvoiceProductDto() {
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setProductId(productSelect.getValue().getId());
        invoiceProductDto.setPrice(priceSelect.getValue().getValue());
        invoiceProductDto.setAmount(amountField.getValue());
        return invoiceProductDto;
    }

    private void updateSaveButtonEnable() {
        saveButton.setEnabled(isFormValid());
    }

    public boolean isFormValid() {
        return productSelect.getValue() != null && priceSelect.getValue() != null && isAmountFieldValid();
    }

    public void clearForm() {
        productSelect.setItems(new ArrayList<>());
        priceSelect.setItems(new ArrayList<>());
        amountField.setValue(BigDecimal.ONE);
        amountField.setInvalid(false);
    }
}
