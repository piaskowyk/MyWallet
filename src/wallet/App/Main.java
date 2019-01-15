package wallet.App;

import javafx.application.Application;
import javafx.stage.Stage;
import wallet.App.Untils.AuthorizationManager;
import wallet.App.Views.ViewsManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AuthorizationManager.authorize();
        ViewsManager.init(primaryStage, getClass());

        if(AuthorizationManager.isAuthorized()){
            ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
        } else {
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        }
        //TODO: wszstkie nazwy pakietów od dużej litery
    }


    public static void main(String[] args) {
        launch(args);
    }
}
