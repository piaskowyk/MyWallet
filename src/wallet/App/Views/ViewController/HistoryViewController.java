package wallet.App.Views.ViewController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wallet.App.Helpers.AuthorizationManager;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewController.Components.Menu;
import wallet.App.Views.ViewController.ThreadsActions.LoadPaymentHistoryThread;

public class HistoryViewController implements IViewController {

    private String imageBasePath = "../src/img/";
    private String imageBasePathForTeared = "../../src/img/";

    @FXML
    private VBox historyContainer;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox menuBar;

    public void initialize(){
        Menu.registerMenu(menuBar);

        if(AuthorizationManager.isAuthorized()){
            LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
            Thread getData = new Thread(loadPaymentHistoryThread);
            Platform.runLater(getData);
        }
    }

    @Override
    public void onLoad() {
        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
        Thread getData = new Thread(loadPaymentHistoryThread);
        Platform.runLater(getData);
    }

    public String getImageBasePath() {
        return imageBasePath;
    }

    public String getImageBasePathForTeared() {
        return imageBasePathForTeared;
    }

    public VBox getHistoryContainer() {
        return historyContainer;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public VBox getMenuBar() {
        return menuBar;
    }
}
