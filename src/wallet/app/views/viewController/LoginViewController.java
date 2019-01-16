package wallet.app.views.viewController;

/*
 * use library:
 * javafx: https://github.com/javafxports/openjdk-jfx
 * */

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import wallet.app.untils.AuthorizationManager;
import wallet.app.views.IViewController;
import wallet.app.views.ViewsManager;
import wallet.commonElements.untils.Validator;

public class LoginViewController implements IViewController {

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

    private EventHandler<MouseEvent> registerBtnOnClick = event -> ViewsManager.loadView(ViewsManager.Views.REGISTER);

    private EventHandler<MouseEvent> loginBtnOnClick = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
            StringBuilder statusList = new StringBuilder();

            if(!Validator.isValidEmail(emailInput.getText())) {
                statusList.append("Invalid email. ");
            }

            if(passwordInput.getText().length() > 40) {
                statusList.append("Password is too long. ");
            }

            if(statusList.length() > 0){
                statusText.setText(statusList.toString());
                return;
            }

            statusText.setText("Waiting...");

            AuthorizationManager.authorize(emailInput.getText(), passwordInput.getText());

            if(!AuthorizationManager.isAuthorized()){
                statusText.setText("Login failed. Try again.");
            } else {
                ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
            }
        }
    };
}
