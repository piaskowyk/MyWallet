package wallet.app.Views.ViewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Views.IViewController;
import wallet.app.Views.ViewsManager;

public class DashboardViewController implements IViewController {

    private String imageBasePath = "Views/img/";

    @FXML
    private ImageView wallet;

    @FXML
    private Pane walletBtn, logoutBtn, historyBtn;

    public void initialize(){
        walletBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, walletBtnOnClick);
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, logoutBtnOnClick);
        historyBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, historyBtnOnClick);
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

//    @Override
//    public void initialize(URL url, ResourceBundle rb)
//    {
////        Image img = new Image(getClass().getResource(imageBasePath + "wallet.png").toExternalForm());
////        wallet.setImage(img);
//    }
}
