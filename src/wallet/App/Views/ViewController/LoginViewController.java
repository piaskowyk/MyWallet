package wallet.App.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import wallet.App.Helpers.AuthorizationManager;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewsManager;
import wallet.CommonEntities.Forms.LoginForm;


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
            LoginForm loginForm = new LoginForm();

            AuthorizationManager.authorize(emailInput.getText(), passwordInput.getText());

            if(!AuthorizationManager.isAuthorized()){
                statusText.setText("LoginForm failed. Try again.");
            } else {
                ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
            }
        }
    };


}
