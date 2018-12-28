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
import wallet.app.Views.ViewController.ThreadsActions.LoadPaymentHistoryThread;
import wallet.app.Views.ViewsManager;

public class HistoryViewController implements IViewController {

    private String imageBasePath = "../src/img/";
    private String imageBasePathForTeared = "../../src/img/";

    @FXML
    private Pane dashboardBtn, logoutBtn, walletBtn;

    @FXML
    private VBox historyContainer;

    @FXML
    private Label statusLabel;

    public void initialize(){
        //menu
        dashboardBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardBtnOnClick);
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, logoutBtnOnClick);
        walletBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, walletBtnOnClick);

        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
        Thread getData = new Thread(loadPaymentHistoryThread);
        Platform.runLater(getData);
    }

    EventHandler dashboardBtnOnClick = event -> ViewsManager.loadView(ViewsManager.Views.DASHBOARD);

    EventHandler logoutBtnOnClick = event -> {
        AuthorizationManager.logOut();
        ViewsManager.loadView(ViewsManager.Views.LOGIN);
    };

    EventHandler walletBtnOnClick = event -> ViewsManager.loadView(ViewsManager.Views.WALLET);

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

    public void setImageBasePath(String imageBasePath) {
        this.imageBasePath = imageBasePath;
    }

    public Pane getDashboardBtn() {
        return dashboardBtn;
    }

    public void setDashboardBtn(Pane dashboardBtn) {
        this.dashboardBtn = dashboardBtn;
    }

    public Pane getLogoutBtn() {
        return logoutBtn;
    }

    public void setLogoutBtn(Pane logoutBtn) {
        this.logoutBtn = logoutBtn;
    }

    public Pane getWalletBtn() {
        return walletBtn;
    }

    public void setWalletBtn(Pane walletBtn) {
        this.walletBtn = walletBtn;
    }

    public VBox getHistoryContainer() {
        return historyContainer;
    }

    public void setHistoryContainer(VBox historyContainer) {
        this.historyContainer = historyContainer;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getImageBasePathForTeared() {
        return imageBasePathForTeared;
    }

    public void setImageBasePathForTeared(String imageBasePathForTeared) {
        this.imageBasePathForTeared = imageBasePathForTeared;
    }
}
