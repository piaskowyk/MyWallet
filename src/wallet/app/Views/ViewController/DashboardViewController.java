package wallet.app.Views.ViewController;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Views.IViewController;
import wallet.app.Views.ViewController.ThreadsActions.LoadDashboardDataThread;
import wallet.app.Views.ViewsManager;

public class DashboardViewController implements IViewController {

    @Override
    public void onLoad() {
        LoadDashboardDataThread loadDashboardDataThread = new LoadDashboardDataThread(this);
        Thread getData = new Thread(loadDashboardDataThread);
        Platform.runLater(getData);
    }

    @FXML
    private Pane walletBtn, logoutBtn, historyBtn;

    @FXML
    private BorderPane areaChartContainer, circleChartContainer;

    @FXML
    private Label accountStateLabel, incomingStatusLabel, outcomingStatusLabel, statusLabel;

    public void initialize(){
        walletBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, walletBtnOnClick);
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, logoutBtnOnClick);
        historyBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, historyBtnOnClick);

        LoadDashboardDataThread loadDashboardDataThread = new LoadDashboardDataThread(this);
        Thread getData = new Thread(loadDashboardDataThread);
        Platform.runLater(getData);
    }

    EventHandler walletBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewsManager.loadView(ViewsManager.Views.WALLET);
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

    public BorderPane getAreaChartContainer() {
        return areaChartContainer;
    }

    public BorderPane getCircleChartContainer() {
        return circleChartContainer;
    }

    public Label getAccountStateLabel() {
        return accountStateLabel;
    }

    public Label getIncomingStatusLabel() {
        return incomingStatusLabel;
    }

    public Label getOutcomingStatusLabel() {
        return outcomingStatusLabel;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

}
