package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@Route(value = "dashboardView", layout = AppView.class)
@PageTitle("Показатели")
@UIScope
public class DashboardView extends VerticalLayout {
}