package wallet.App.Views.ViewController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import wallet.App.Helpers.Postman;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewsManager;
import wallet.CommonEntities.Forms.RegisterForm;
import wallet.CommonEntities.Responses.DataResponses.StandardResult;


public class RegisterViewController implements IViewController {

    @FXML
    private Button sendRegisterBtn;

    @FXML
    private Label returnBtn, statusText;

    @FXML
    private TextField nameInput, surnameInput, emailInput, passwordInput;

    public void initialize(){
        returnBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, returnBtnOnClick);
        sendRegisterBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, sendRegisterBtnOnClick);
    }

    private EventHandler<MouseEvent> returnBtnOnClick = event -> ViewsManager.loadView(ViewsManager.Views.LOGIN);

    private EventHandler<MouseEvent> sendRegisterBtnOnClick = new EventHandler<>(){
        @Override
        public void handle(MouseEvent event) {
            statusText.setText("Waiting...");

            RegisterForm registerForm = new RegisterForm();
            registerForm.setName(nameInput.getText());
            registerForm.setSurname(surnameInput.getText());
            registerForm.setEmail(emailInput.getText());
            registerForm.setPassword(passwordInput.getText());

            Postman<StandardResult> postman = new Postman<>(false);
            StandardResult loginResponse = postman.send(registerForm, StandardResult.class, Postman.Api.REGISTER);

            if(postman.noError() && loginResponse.getStatus()){
                statusText.setText("Account is ready to login.");
            } else {
                statusText.setText(postman.getErrorMessage());
            }

        }
    };

}
