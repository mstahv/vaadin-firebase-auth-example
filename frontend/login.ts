import { html, LitElement } from 'lit';
import {customElement, query} from 'lit/decorators.js';
import { LoginI18n, LoginOverlay } from '@vaadin/vaadin-login/vaadin-login-overlay.js';

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";

// Your web app's Firebase configuration goes here:
const firebaseConfig = {
  apiKey: "AIzaSyB2PRrS6sk-Hgtvxv__FViNCHBh7Esvv6o",
  authDomain: "fir-vaadinexample.firebaseapp.com",
  projectId: "fir-vaadinexample",
  storageBucket: "fir-vaadinexample.appspot.com",
  messagingSenderId: "344504568176",
  appId: "1:344504568176:web:68ee967ce7f9e4707fe7b5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

@customElement('login-view')
class LoginView extends LitElement {

    private i18n: LoginI18n = {
      header: {
        title: 'Vaadin + Firebase Auth example',
        description: 'Automatically inserted demo credentials: admin@test.com/admin1',
      },
        form: {
        title: 'Sign in',
        username: 'Email',
        password: 'Password',
        submit: 'Login',
        forgotPassword: '',
      },
      errorMessage: {
        title: 'Wrong email/password',
        message: 'Check your credentials and try again..',
      },
    };

    render() {
        return html`
        <vaadin-login-overlay opened .i18n="${this.i18n}" @login="${this._login}"></vaadin-login-overlay>
`;
    }

    @query('vaadin-login-overlay')
    private login?: LoginOverlay;


    private _login(e: CustomEvent) {
        const auth = getAuth();
       
        signInWithEmailAndPassword(auth, e.detail.username, e.detail.password)
          .then((userCredential) => {
            // Signed in
            const user : any = userCredential.user;

             // Pass the token for the server side app for validation & login
             // @ts-ignore
             this.$server.login(user.accessToken, user.uid);
          })
          .catch((error) => {
            const errorCode = error.code;
            const errorMessage = error.message;
            if(this.login) {
              this.login.disabled = false;
              this.login.error = true;
            }
          });
    }
}
