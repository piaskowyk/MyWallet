package wallet.app.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import wallet.app.exceptions.NoImplementsInterfaceException;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

public class ViewsManager {

    private static Stage _primaryStage;
    private static Class _mainClass;
    private static HashMap<String, Scene> allScene = new HashMap<>();
    private static HashMap<String, IViewController> allController = new HashMap<>();

    private ViewsManager(){}

    public static void init(Stage primaryStage, Class mainClass){
        _primaryStage = primaryStage;
        _mainClass = mainClass;

        registerAllViews();
    }

    public static void clearViewData(){
        allController.clear();

        registerAllViews();
    }

    private static void registerAllViews(){
        EnumSet.allOf(Views.class).forEach(ViewsManager::registerScene);
    }

    public static void loadView(Views view) {
        _primaryStage.setScene(allScene.get(view.path));
        _primaryStage.setTitle(view.title);
        _primaryStage.show();
        //execute methods from Interface
        allController.get(view.path).onLoad();
    }

    private static void registerScene(Views view){
        try {
            FXMLLoader loader = new FXMLLoader(_mainClass.getResource("views/src/" + view.path + ".fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(_mainClass.getResource("views/src/style.css").toExternalForm());
            scene.getStylesheets().add(_mainClass.getResource("views/src/" + view.cssPath + ".css").toExternalForm());
            allScene.put(view.path, scene);

            if(!(loader.getController() instanceof IViewController)) throw new NoImplementsInterfaceException();
            allController.put(view.path, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Views{
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