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
        System.out.println(AuthorizationManager.isAuthorized());
        if(AuthorizationManager.isAuthorized()){
            ViewsManager.loadView(ViewsManager.Views.DASHBOARD);
        } else {
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
