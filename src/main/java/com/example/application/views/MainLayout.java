package com.example.application.views;

import com.example.application.security.AuthenticatedUser;
import com.google.firebase.auth.FirebaseToken;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import org.vaadin.firitin.appframework.NavigationItem;

import java.util.Optional;

public class MainLayout extends org.vaadin.firitin.appframework.MainLayout {

    @Override
    protected String getDrawerHeader() {
        return "Vaadin + Firebase Auth";
    }

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
    }

    @Override
    protected boolean checkAccess(NavigationItem navigationItem) {
        return accessChecker.hasAccess(navigationItem.getNavigationTarget());
    }

    @Override
    protected Footer createFooter() {
        Footer footer = super.createFooter();

        Optional<FirebaseToken> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            FirebaseToken user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName(), user.getPicture());
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Logout", e -> {
                authenticatedUser.logout();
            });

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            footer.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            footer.add(loginLink);
        }

        return footer;
    }

}
