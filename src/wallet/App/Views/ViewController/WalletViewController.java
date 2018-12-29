package wallet.App.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import wallet.App.Helpers.Postman;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewController.Components.Menu;
import javafx.scene.control.Button;
import wallet.CommonEntities.Forms.PaymentForm;
import wallet.CommonEntities.Responses.DataResponses.StandardResult;

public class WalletViewController implements IViewController {

    @FXML
    private TextField inPaymentAmount, inPaymentTitle, outPaymentAmount, outPaymentTitle;

    @FXML
    private Button inPaymentBtn, outPaymentBtn;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox menuBar;

    public void initialize(){
        Menu.registerMenu(menuBar);

        //panel action
        inPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, inPaymentBtnOnClick);
        outPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, outPaymentBtnOnClick);

    }

    private EventHandler inPaymentBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(inPaymentAmount.getText()));
            paymentForm.setTitle(inPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.INCOMING);

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

    private EventHandler outPaymentBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(outPaymentAmount.getText()));
            paymentForm.setTitle(outPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.OUTCOMING);

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

    public TextField getInPaymentAmount() {
        return inPaymentAmount;
    }

    public TextField getInPaymentTitle() {
        return inPaymentTitle;
    }

    public TextField getOutPaymentAmount() {
        return outPaymentAmount;
    }

    public TextField getOutPaymentTitle() {
        return outPaymentTitle;
    }

    public Button getInPaymentBtn() {
        return inPaymentBtn;
    }

    public Button getOutPaymentBtn() {
        return outPaymentBtn;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public VBox getMenuBar() {
        return menuBar;
    }
}
