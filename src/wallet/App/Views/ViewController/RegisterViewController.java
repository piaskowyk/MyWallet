package wallet.App.Views.ViewController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import wallet.App.Untils.Postman;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewsManager;
import wallet.CommonElements.Forms.RegisterForm;
import wallet.CommonElements.Untils.Validator;
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

            StringBuilder statusList = new StringBuilder();

            if(!Validator.isValidAlphaStringPersonInfo(nameInput.getText(), 100)) {
                statusList.append("Invalid name.");
            }

            if(!Validator.isValidAlphaStringPersonInfo(surnameInput.getText(), 100)) {
                statusList.append("Invalid surname.");
            }

            if(!Validator.isValidEmail(emailInput.getText())) {
                statusList.append("Invalid email.");
            }

            if(passwordInput.getText().length() < 5) {
                statusList.append("Password is too short, minimum 5 characters.");
            }

            if(passwordInput.getText().length() > 40) {
                statusList.append("Password is too long, maximum 40 characters.");
            }

            if(!passwordInput.getText().equals(retypePasswordInput.getText())){
                statusList.append("Retype password is different.");
            }

            if(statusList.length() > 0){
                statusText.setText(statusList.toString());
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
