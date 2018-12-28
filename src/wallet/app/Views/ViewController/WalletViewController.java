package wallet.app.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wallet.app.Exceptions.UnauthorizationRequestException;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Helpers.Postman;
import wallet.app.Views.IViewController;
import wallet.app.Views.ViewController.Components.Menu;
import wallet.app.Views.ViewsManager;
import javafx.scene.control.Button;
import wallet.server.Forms.PaymentForm;
import wallet.server.Responses.DataResponses.StandardResult;

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

    EventHandler inPaymentBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(inPaymentAmount.getText()));
            paymentForm.setTitle(inPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.INCOMING);

            Postman<StandardResult> postman = new Postman<StandardResult>();
            StandardResult loginResponse = null;
            try {
                loginResponse = postman.send(paymentForm, StandardResult.class, Postman.Api.ADD_PAYMENTS);
            } catch (UnauthorizationRequestException e) {
                statusLabel.setText("You don't have permission to this operation.");
            }

            if(loginResponse.getStatus()){
                statusLabel.setText("OK, add new payment.");
                inPaymentAmount.setText("");
                inPaymentTitle.setText("");
            }
            else {
                statusLabel.setText("Incorrect data.");
            }
        }
    };

    EventHandler outPaymentBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            statusLabel.setText("Waiting...");

            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setAmount(Float.parseFloat(outPaymentAmount.getText()));
            paymentForm.setTitle(outPaymentTitle.getText());
            paymentForm.setType(PaymentForm.Type.OUTCOMING);

            Postman<StandardResult> postman = new Postman<StandardResult>();
            StandardResult loginResponse = null;
            try {
                loginResponse = postman.send(paymentForm, StandardResult.class, Postman.Api.ADD_PAYMENTS);
            } catch (UnauthorizationRequestException e) {
                statusLabel.setText("You don't have permission to this operation.");
            }

            if(loginResponse.getStatus()){
                statusLabel.setText("OK, add new payment.");
                outPaymentAmount.setText("");
                outPaymentTitle.setText("");
            }
            else {
                statusLabel.setText("Incorrect data.");
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
