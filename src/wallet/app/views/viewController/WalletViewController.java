package wallet.app.views.viewController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import wallet.app.untils.Postman;
import wallet.app.views.IViewController;
import wallet.app.views.viewController.components.Menu;
import wallet.commonElements.entity.PaymentCategory;
import wallet.commonElements.forms.PaymentForm;
import wallet.commonElements.untils.Validator;
import wallet.commonElements.responses.dataResponses.StandardResult;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.EnumSet;

public class WalletViewController implements IViewController {

    @FXML
    private TextField inPaymentAmount, inPaymentTitle, outPaymentAmount, outPaymentTitle;

    @FXML
    private Button inPaymentBtn, outPaymentBtn;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox menuBar;

    @FXML
    private DatePicker inPaymentDate, outPaymentDate;

    @FXML
    private ChoiceBox<String> selectCategory;

    public void initialize(){
        Menu.registerMenu(menuBar);

        //panel action
        inPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, inPaymentBtnOnClick);
        outPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, outPaymentBtnOnClick);

        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        inPaymentDate.setValue(localDate);
        outPaymentDate.setValue(localDate);

        EnumSet.allOf(PaymentCategory.class)
                .forEach(item -> {
                    if(item != PaymentCategory.IN){
                        selectCategory.getItems().add(item.getName());
                    }
                });
        selectCategory.getSelectionModel().selectFirst();
    }

    private EventHandler<MouseEvent> inPaymentBtnOnClick = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {

            if(inPaymentTitle.getText().length() > 5000){
                statusLabel.setText("Too long title.");
                return;
            }

            if(!Validator.isValidFloatNum(inPaymentAmount.getText())){
                statusLabel.setText("Invalid amount.");
                return;
            }

            if(!Validator.isDataPicker(inPaymentDate.getValue().toString())){
                statusLabel.setText("Invalid data.");
                return;
            }

            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(inPaymentAmount.getText()));
            paymentForm.setTitle(inPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.INCOMING);
            paymentForm.setDate(inPaymentDate.getValue().toString());
            paymentForm.setCategory(PaymentCategory.IN);

            Postman<StandardResult> postman = new Postman<>();
            StandardResult loginResponse = postman.send(paymentForm, StandardResult.class, Postman.Api.ADD_PAYMENTS);

            if(postman.noError() && loginResponse.getStatus()){
                statusLabel.setText("OK, add new payment.");
                inPaymentAmount.setText("");
                inPaymentTitle.setText("");
            }
            else {
                statusLabel.setText(postman.getErrorMessage());
            }
        }
    };

    private EventHandler<MouseEvent> outPaymentBtnOnClick = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {

            StringBuilder statusList = new StringBuilder();

            if(outPaymentTitle.getText().length() > 5000){
                statusList.append("Too long title.");
            }

            if(!Validator.isValidFloatNum(outPaymentAmount.getText())){
                statusList.append("Invalid amount.");
            }

            if(!Validator.isDataPicker(outPaymentDate.getValue().toString())){
                statusList.append("Invalid data.");
            }

            if(statusList.length() > 0){
                statusLabel.setText(statusList.toString());
                return;
            }

            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(outPaymentAmount.getText()));
            paymentForm.setTitle(outPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.OUTCOMING);
            paymentForm.setDate(inPaymentDate.getValue().toString());
            paymentForm.setCategory(selectCategory.getValue().toString());

            Postman<StandardResult> postman = new Postman<>();
            StandardResult loginResponse = postman.send(paymentForm, StandardResult.class, Postman.Api.ADD_PAYMENTS);

            if(postman.noError() && loginResponse.getStatus()){
                statusLabel.setText("OK, add new payment.");
                outPaymentAmount.setText("");
                outPaymentTitle.setText("");
            }
            else {
                statusLabel.setText(postman.getErrorMessage());
            }
        }
    };
}
