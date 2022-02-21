# Vaadin + Firebase Auth example

A trivial example to use Firebase Authentication with a Vaadin application.
The app is built based on start.vaadin.com template, with Spring Security 
setup enabled, but instead of storing users in local JPA backend, Firebase
Authentication is used.

This example uses the "web" library to create the access token on the browser
side and then shares that with the Vaadin application. An alternative approach 
would be to implement this completely on the server side by doing to the 
authentication request as described in [this SoF response](https://stackoverflow.com/questions/40824660/firebase-user-authentication-for-java-application-not-android) 
or by checking if the official Android library could be used.

Things to check out in this example.

 * _FirebaseService_ class. A class that initializes Firebase server side API, 
   populates demo data if needed, and validates the token coming from client.
   and transforms into Authentication object for Spring Security.
 * _LoginView_ and its client side code in _frontend/login.ts_. This uses
   Vaadin Login element (could just use simple inputs as well) and 
   Firebase JS modules to login. The token is then sent to server,
   validated and stores an Authentication based on the token details in 
   Spring Security context (saved further in Java session).
 * Adapted Spring Security configuartion in the _security_ package.

### Improvements needed for larger app

 * Currently the example doesn't support roles, but those should be rather
   easy to implement with "custom claims".
 * Session persistency. Currently, the token passed to server side will 
   probably become invalid even if user Vaadin session is active. Should
   timely pass the refreshed token to the server side. Service worker
   appraoch could be used. Or just fetching the refreshed token 
   asynchronously when the session should be verified.

## Running the application

**To run the example yourself, you'll need to create a Firebase Project and
enable Firebase Authentication (refer to their docs). The
GOOGLE_APPLICATION_CREDENTIALS environment variable needs to contain the 
path to your app credentials (or change the behaviour in FirebaseService) and
you need to replace the Web/JS firebaseConfig with your Firebase web config in login.ts file.**

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
