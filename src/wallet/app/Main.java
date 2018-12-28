package wallet.app;

import javafx.application.Application;
import javafx.stage.Stage;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Views.ViewsManager;

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
        //TODO: duzo razy pokazuje się no internect connection jeśli server jest wyłaczony
    }


    public static void main(String[] args) {
        launch(args);
    }
}
