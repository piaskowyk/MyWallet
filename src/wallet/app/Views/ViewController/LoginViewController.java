package wallet.app.Views.ViewController;

import com.google.gson.Gson;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Helpers.Postman;
import wallet.app.Views.ViewsManager;
import wallet.server.Forms.Login;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import wallet.server.Responses.DataResponses.LoginResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class LoginViewController {
    @FXML
    private Button loginBtn;

    @FXML
    private Label registerBtn, statusText;

    @FXML
    private TextField emailInput, passwordInput;


    public void initialize(){
        registerBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, registerBtnOnClick);
        loginBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, loginBtnOnClick);
    }

    EventHandler registerBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewsManager.loadView(ViewsManager.Views.REGISTER);
        }
    };

    EventHandler loginBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            statusText.setText("Waiting...");
            Login loginForm = new Login();

            AuthorizationManager.authorize(emailInput.getText(), passwordInput.getText());

            if(!AuthorizationManager.isAuthorized()){
                statusText.setText("Login failed. Try again.");
            } else {
                ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
            }
        }
    };


}
