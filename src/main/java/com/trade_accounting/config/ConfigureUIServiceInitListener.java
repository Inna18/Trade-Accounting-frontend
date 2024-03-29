package com.trade_accounting.config;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.MainLayout;
import com.trade_accounting.components.authentication.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.stereotype.Component;

import static com.trade_accounting.config.SecurityConstants.BODY_URL;
import static com.trade_accounting.config.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

        if (wrappedSession.getAttribute(TOKEN_ATTRIBUTE_NAME) == null) {
            if (AppView.class.equals(event.getNavigationTarget())) {
                System.out.println("Я НЕ авторизован и сейчас тут (AppView.class): " + event.getNavigationTarget());
                System.out.println("---------------------------------------------");
                event.rerouteTo(LoginView.class);
            }
            if (MainLayout.class.equals(event.getNavigationTarget())) {
                System.out.println("Я НЕ авторизован и сейчас тут (MainLayout.class): " + event.getNavigationTarget());
                System.out.println("---------------------------------------------");
                UI.getCurrent().navigate(MainLayout.class);
            }
            if (!AppView.class.equals(event.getNavigationTarget()) & !MainLayout.class.equals(event.getNavigationTarget())) {
                event.rerouteTo(LoginView.class);
                UI.getCurrent().navigate(LoginView.class);
            }
        } else {

            if (event.getLocation().getPath().equals("")) {
                System.out.println("Я УЖЕ авторизован и сейчас нахожусь на http://localhost:4445/: " + event.getNavigationTarget());
                System.out.println("---------------------------------------------");
                event.rerouteTo(AppView.class);
                //Если не добавить эту строку, то переход на необходимую страницу будет совершен, но URL не изменится!
                UI.getCurrent().navigate(AppView.class);
            }
        }
    }
}

