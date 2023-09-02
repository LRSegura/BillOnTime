package com.code2ever.bot.views;

import com.code2ever.bot.api.security.SecurityService;
import com.code2ever.bot.views.batch.BatchView;
import com.code2ever.bot.views.bill.BillView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final AuthenticationContext authContext;
    private final SecurityService securityService;
    private H2 viewTitle;

    public MainLayout(AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
//        System.out.println("password:" + new BCryptPasswordEncoder().encode("12345"));
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();


    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
//        if (securityService.getAuthenticatedUser() != null) {
//            Button logout = new Button("Logout", click ->
//                    securityService.logout());
//            HorizontalLayout header = new HorizontalLayout(logout);
//            addToNavbar(true, header);
//        }

//        HorizontalLayout header = authContext.getAuthenticatedUser(UserDetails.class).map(user -> {
//            Button logout = new Button("Logout", click -> this.authContext.logout());
//            Span loggedUser = new Span("Welcome " + user.getUsername());
//            HorizontalLayout layout = new HorizontalLayout(FlexComponent.JustifyContentMode.END, loggedUser, logout);
//            layout.setWidthFull();
//            return layout;
//        }).orElseGet(() -> new HorizontalLayout());
        HorizontalLayout header = authContext.getPrincipalName().map(user -> {
            Button logout = new Button("Logout", click -> this.authContext.logout());
            Span loggedUser = new Span("Welcome " + user);
            HorizontalLayout layout = new HorizontalLayout(FlexComponent.JustifyContentMode.END, loggedUser, logout);
            layout.setWidthFull();
            return layout;
        }).orElseGet(() -> new HorizontalLayout());
        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("BOT");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
//        nav.addItem(new SideNavItem("Hello World", HomeView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        nav.addItem(new SideNavItem("Bill", BillView.class, LineAwesomeIcon.CALCULATOR_SOLID.create()));
        nav.addItem(new SideNavItem("Batch", BatchView.class, LineAwesomeIcon.CALCULATOR_SOLID.create()));
        return nav;
    }

    private Footer createFooter() {
        return new Footer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
