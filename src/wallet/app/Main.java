package wallet.app;

/*
* use library:
* javafx: https://github.com/javafxports/openjdk-jfx
* */

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import wallet.app.untils.AuthorizationManager;
import wallet.app.views.ViewsManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        //autoryzuj użytkownika
        AuthorizationManager.authorize();
        //załaduj widoki
        ViewsManager.init(primaryStage, getClass());

        //uruchom widok startowy
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
