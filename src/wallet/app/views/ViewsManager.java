package wallet.app.views;

/*
 * use library:
 * javafx: https://github.com/javafxports/openjdk-jfx
 * */

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import wallet.app.exceptions.NoImplementsInterfaceException;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

public class ViewsManager {

    private static Stage primaryStage;
    private static Class mainClass;
    private static HashMap<String, Scene> allScene = new HashMap<>();
    private static HashMap<String, IViewController> allController = new HashMap<>();
    private static int sceneWidth = 1200;
    private static int sceneHeight = 800;

    private ViewsManager(){}

    public static void init(Stage mainPrimaryStage, Class mainClass){
        primaryStage = mainPrimaryStage;
        mainClass = mainClass;

        registerAllViews();

        primaryStage.setWidth(sceneWidth);
        primaryStage.setHeight(sceneHeight);

        primaryStage.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            sceneWidth = newSceneWidth.intValue();
        });
        primaryStage.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            sceneHeight = newSceneHeight.intValue();
        });
    }

    public static void clearViewData(){
        allController.clear();

        registerAllViews();
    }

    private static void registerAllViews(){
        EnumSet.allOf(Views.class).forEach(ViewsManager::registerScene);
    }

    public static void loadView(Views view) {
        primaryStage.setScene(allScene.get(view.path));
        primaryStage.setTitle(view.title);
        primaryStage.show();
        primaryStage.setWidth(sceneWidth);
        primaryStage.setHeight(sceneHeight);
        //dodanie nowej akcji onLoad dla kontrolerów widoku, po zmianie widoku w menu
        // dzięki temu że dziedziczą z interfejsu IViewController
        allController.get(view.path).onLoad();
    }

    private static void registerScene(Views view){
        //ładowanie widoku z pliku fxml, wraz z plikami konfiguracyjnymi
        try {
            FXMLLoader loader = new FXMLLoader(mainClass.getResource("views/src/" + view.path + ".fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(mainClass.getResource("views/src/style.css").toExternalForm());
            scene.getStylesheets().add(mainClass.getResource("views/src/" + view.cssPath + ".css").toExternalForm());
            allScene.put(view.path, scene);

            if(!(loader.getController() instanceof IViewController)) throw new NoImplementsInterfaceException();
            allController.put(view.path, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Views{
        //dostępne widoki, wraz z konfiguracją zdefiniowana dla każdego
        LOGIN("login", "login", "LoginForm to MyWallet"),
        REGISTER("register", "register", "Create new MyWallet account"),
        DASHBOARD("dashboard", "dashboard", "MyWallet"),
        WALLET("wallet", "wallet", "MyWallet"),
        HISTORY("history", "history", "MyWallet");

        String path;
        String cssPath;
        String title;

        Views(String path, String cssPath, String title){
            this.path = path;
            this.cssPath = cssPath;
            this.title = title;
        }
    }

}