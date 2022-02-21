package com.example.application;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Service
public class FirebaseService {

    private FirebaseApp app;

    @PostConstruct
    void init() {
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();

            app = FirebaseApp.initializeApp(options);
            // Ensure we have a admin@test.com/admin1 account for testing,
            // create if not available

            try {
                UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail("admin@test.com");
                System.out.println("User admin@test.com already exists. Uid: " + userRecord.getUid());
            } catch (FirebaseAuthException e) {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail("admin@test.com")
                        .setEmailVerified(false)
                        .setPassword("admin1")
                        .setPhoneNumber("+11234567890")
                        .setDisplayName("John Doe")
                        .setPhotoUrl("http://www.example.com/12345678/photo.png")
                        .setDisabled(false);

                UserRecord userRecord = null;
                try {
                    userRecord = FirebaseAuth.getInstance().createUser(request);
                    System.out.println("Successfully created new admin user: " + userRecord.getUid());
                } catch (FirebaseAuthException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserRecord findUserByEmail(String username) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUserByEmail(username);
    }

    public Authentication login(String token) throws FirebaseAuthException {
        boolean checkRevoked = true;
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token, checkRevoked);
        // Note, before important actions, the token should be re-validate,
        // it could be revoked or the user could be removed altogether,
        // during their session

        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.EMPTY_SET;
            }

            @Override
            public Object getCredentials() {
                return token;
            }

            @Override
            public Object getDetails() {
                return firebaseToken;
            }

            @Override
            public Object getPrincipal() {
                return firebaseToken.getUid();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return firebaseToken.getName();
            }
        };
    }
}
