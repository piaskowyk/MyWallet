package wallet.app.Views.ViewController;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Views.IViewController;
import wallet.app.Views.ViewController.Components.Menu;
import wallet.app.Views.ViewController.ThreadsActions.LoadPaymentHistoryThread;
import wallet.app.Views.ViewsManager;

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

        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
        Thread getData = new Thread(loadPaymentHistoryThread);
        Platform.runLater(getData);
    }

    @Override
    public void onLoad() {
        System.out.println("history load");
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
