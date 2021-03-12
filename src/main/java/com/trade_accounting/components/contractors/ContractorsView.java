package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div implements AfterNavigationObserver {

    private final Div div;

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final ContractsView contractsView;

    @Autowired
    public ContractorsView(ContractorService contractorService,
                           ContractorGroupService contractorGroupService,
                           ContractsView contractsView) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        this.contractsView = contractsView;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
//            div.removeAll();
//            div.add(new ContractorsTabView(contractorService, contractorGroupService));

            AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
            appView.getChildren().forEach(e -> {
                if (e.getClass() == Tabs.class) {
                    ((Tabs) e).setSelectedIndex(4);
                }
            });
            getUI().ifPresent(ui -> {
                div.removeAll();
                div.add(new ContractorsTabView(contractorService, contractorGroupService));
            });
    }

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(new Tab("Контрагенты"), new Tab("Договоры"));

        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Контрагенты":
                    div.removeAll();
                    div.add(new ContractorsTabView(contractorService,contractorGroupService));
                    break;
                case "Договоры":
                    div.removeAll();
                    div.add(contractsView);
                    break;
            }
        });
        return tabs;
    }
}
