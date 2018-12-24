package wallet.app.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Helpers.Postman;
import wallet.app.Views.ViewsManager;
import wallet.server.Forms.Payment;
import wallet.server.Responses.DataResponses.StandardResult;

public class HistoryViewController {

    @FXML
    private Pane dashboardBtn, logoutBtn, walletBtn;

    @FXML
    private VBox historyContainer;


    public void initialize(){
        //menu
        dashboardBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardBtnOnClick);
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, logoutBtnOnClick);
        walletBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, walletBtnOnClick);

        Pane pane = new Pane();
        pane.setMinHeight(100);
        pane.setStyle("-fx-background-color: #123123;");
        historyContainer.getChildren().add(pane);

        //wywołaj metodę pobierz na nowym wątku,
        //wątek po pobraniu danych wywołuje statyczną metodę w kontrolerze która wygeneruje historię na podstawie pobranych danych i wyświetli ją w postaci listy
        //genialne XDDD

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

    EventHandler walletBtnOnClick = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewsManager.loadView(ViewsManager.Views.WALLET);
        }
    };

}
