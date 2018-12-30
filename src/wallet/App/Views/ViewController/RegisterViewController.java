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
import wallet.CommonElements.Forms.RegisterForm;
import wallet.CommonElements.Helpers.Validator;
import wallet.CommonElements.Responses.DataResponses.StandardResult;


public class RegisterViewController implements IViewController {

    @FXML
    private Button sendRegisterBtn;

    @FXML
    private Label returnBtn, statusText;

    @FXML
    private TextField nameInput, surnameInput, emailInput, passwordInput, retypePasswordInput;

    public void initialize(){
        returnBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, returnBtnOnClick);
        sendRegisterBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, sendRegisterBtnOnClick);
    }

    private EventHandler<MouseEvent> returnBtnOnClick = event -> ViewsManager.loadView(ViewsManager.Views.LOGIN);

    private EventHandler<MouseEvent> sendRegisterBtnOnClick = new EventHandler<>(){
        @Override
        public void handle(MouseEvent event) {

            if(!Validator.isValidAlphaStringPersonInfo(nameInput.getText(), 100)) {
                statusText.setText("Invalid name.");
                return;
            }

            if(!Validator.isValidAlphaStringPersonInfo(surnameInput.getText(), 100)) {
                statusText.setText("Invalid surname.");
                return;
            }

            if(!Validator.isValidEmail(emailInput.getText())) {
                statusText.setText("Invalid email.");
                return;
            }

            if(passwordInput.getText().length() < 5) {
                statusText.setText("Password is too short, minimum 5 characters.");
                return;
            }

            if(passwordInput.getText().length() > 40) {
                statusText.setText("Password is too long, maximum 40 characters.");
                return;
            }

            if(!passwordInput.getText().equals(retypePasswordInput.getText())){
                statusText.setText("Retype password is different.");
                return;
            }

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
