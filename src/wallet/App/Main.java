package wallet.App;

import javafx.application.Application;
import javafx.stage.Stage;
import wallet.App.Helpers.AuthorizationManager;
import wallet.App.Views.ViewsManager;

public class Main extends Application {

    private boolean isLog = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AuthorizationManager.authorize();
        ViewsManager.init(primaryStage, getClass());

        if(AuthorizationManager.isAuthorized()){
            ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
        } else {
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        }
        //TODO: poprawić obsługę wyjątków i komunikatow dawanych urzytkownikowi
        //TODO: walidacja danych, dodać obsługę po stronie frontu i sprawdzić po stronie backu
    }


    public static void main(String[] args) {
        launch(args);
    }
}
