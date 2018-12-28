package wallet.app.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import wallet.app.Exceptions.UnauthorizationRequestException;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Helpers.Postman;
import wallet.app.Views.IViewController;
import wallet.app.Views.ViewsManager;
import javafx.scene.control.Button;
import wallet.server.Forms.PaymentForm;
import wallet.server.Responses.DataResponses.StandardResult;

public class WalletViewController implements IViewController {

    private String imageBasePath = "Views/img/";

    @FXML
    private ImageView wallet;

    @FXML
    private Pane dashboardBtn, logoutBtn, historyBtn;

    @FXML
    private TextField inPaymentAmount, inPaymentTitle, outPaymentAmount, outPaymentTitle;

    @FXML
    private Button inPaymentBtn, outPaymentBtn;

    @FXML
    private Label statusLabel;

    public void initialize(){
        //menu
        dashboardBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardBtnOnClick);
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, logoutBtnOnClick);
        historyBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, historyBtnOnClick);

        //panel action
        inPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, inPaymentBtnOnClick);
        outPaymentBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, outPaymentBtnOnClick);

    }

    EventHandler dashboardBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
        }
    };

    EventHandler logoutBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            AuthorizationManager.logOut();
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        }
    };

    EventHandler historyBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewsManager.loadView(ViewsManager.Views.HISTORY);
        }
    };

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

}
