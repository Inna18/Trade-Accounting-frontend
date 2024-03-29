package com.trade_accounting.components.indicators;


import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import static com.trade_accounting.config.SecurityConstants.INDICATORS;

@Route(value = INDICATORS, layout = AppView.class)
@PageTitle("Показатели")
@SpringComponent
@UIScope
public class IndicatorsSubMenuView extends Div implements AfterNavigationObserver {
    private final DashboardView dashboardView;
    private final OperationsView operationsView;
    private final RecyclebinView recyclebinView;
    private final AuditView auditView;
    private final DocumentsView documentsView;
    private final Div div = new Div();

    // Аннотации Lazy отображают страницу, когда мы на неё переходим. Без этой аннотации нет отображения страницы.
    // Она проставлена для удобства решения задачи с безопасностью.
    @Autowired
    public IndicatorsSubMenuView(@Lazy DashboardView dashboardView,
                          @Lazy OperationsView operationsView,
                          @Lazy RecyclebinView recyclebinView,
                          @Lazy AuditView auditView,
                          @Lazy DocumentsView documentsView) {
        this.dashboardView = dashboardView;
        this.operationsView = operationsView;
        this.recyclebinView = recyclebinView;
        this.auditView = auditView;
        this.documentsView = documentsView;
        add(configurationSubMenu(), div);
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout dashboardTab = new HorizontalLayout(new Label("Показатели"));

        dashboardTab.addClickListener(e ->
                dashboardTab.getUI().ifPresent(ui -> {
                    div.removeAll();
//                  dashboardTab.updateData();
                    div.add(dashboardView);
                }));

        HorizontalLayout operationsTab = new HorizontalLayout(new Label("Документы"));
        operationsTab.addClickListener(e ->
                operationsTab.getUI().ifPresent(ui -> {
                    div.removeAll();
//                   operationsTab.updateData();
                    div.add(operationsView);
                }));

        HorizontalLayout recyclebinTab = new HorizontalLayout(new Label("Корзина"));
        recyclebinTab.addClickListener(e ->
                recyclebinTab.getUI().ifPresent(ui -> {
                    div.removeAll();
//                   recyclebinTab.updateData();
                    recyclebinView.updateList();
                    div.add(recyclebinView);
                }));

        HorizontalLayout auditTab = new HorizontalLayout(new Label("Аудит"));
        auditTab.addClickListener(e ->
                auditTab.getUI().ifPresent(ui -> {
                    div.removeAll();
//                   recyclebinTab.updateData();
                    div.add(auditView);
                }));

        HorizontalLayout documentsTab = new HorizontalLayout(new Label("Файлы"));
        documentsTab.addClickListener(e ->
                documentsTab.getUI().ifPresent(ui -> {
                    div.removeAll();
//                   documentsTab.updateData();
                    div.add(documentsView);
                }));

        return new Tabs(
                new Tab(dashboardTab),
                new Tab(operationsTab),
                new Tab(recyclebinTab),
                new Tab(auditTab),
                new Tab(documentsTab)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(0);
            }
        });
    }
}
