package com.example.application.views.login;

import com.example.application.FirebaseService;
import com.example.application.views.helloworld.HelloWorldView;
import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route
@NpmPackage(value = "firebase", version = "9.6.7")
@Tag("login-view")
@JsModule("./login.ts")
public class LoginView extends LitTemplate {

    private final FirebaseService firebase;

    public LoginView(FirebaseService firebaseService) {
        this.firebase = firebaseService;
    }

    @ClientCallable
    private void login(String token, String uid) {

        try {
            // Validate the token at Firebase server  first and create
            // Authentication object based on it
            Authentication authentication = firebase.login(token);
            // Save the authentication to context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UI.getCurrent().navigate(HelloWorldView.class);

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid token!");
        }

    }

}
