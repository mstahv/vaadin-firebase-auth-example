# Vaadin + Firebase Auth example

A trivial example to use Firebase Authentication with a Vaadin application.
The app is built based on start.vaadin.com template, with Spring Security 
setup enabled, but instead of storing users in local JPA backend, Firebase
Authentication is used.

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

## Running the application

**To run the example yourself, you'll need to create a Firebase Project and
enable Firebase Authentication (refer to their docs). The
GOOGLE_APPLICATION_CREDENTIALS environment variable needs to contain the 
path to your app credentials (or change the behaviour in FirebaseService) and
you need to replace the Web/JS firebaseConfig with your Firebase web config in login.ts file.**

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
