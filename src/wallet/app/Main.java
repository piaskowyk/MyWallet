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
        //TODO: Dodac datę do płątności
        //TODO: obsługa polskich znaków
        //TODO: po wylogowaniu od nowa trzaba załadować widoki bo zostaną stare dane bo zapisane są w ramie
        //TODO: poprawić obsługę wyjątków i komunikatow dawanych urzytkownikowi
        //TODO walidacja danych, dodać obsługę po stronie frontu i sprawdzić po stronie backu
    }


    public static void main(String[] args) {
        launch(args);
    }
}
