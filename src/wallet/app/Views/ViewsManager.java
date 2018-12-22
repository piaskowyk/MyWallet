package wallet.app.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewsManager {

    private static Stage _primaryStage;
    private static Class _mainClass;

    private ViewsManager(){}

    public static void init(Stage primaryStage, Class mainClass){
        _primaryStage = primaryStage;
        _mainClass = mainClass;
    }

    public static void loadView(Views view) {
        try {
            FXMLLoader loader = new FXMLLoader(_mainClass.getResource("Views/src/" + view.path + ".fxml"));
            BorderPane root = null;
            root = (BorderPane) loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(_mainClass.getResource("Views/src/style.css").toExternalForm());
            scene.getStylesheets().add(_mainClass.getResource("Views/src/" + view.cssPath + ".css").toExternalForm());
            _primaryStage.setScene(scene);
            _primaryStage.setTitle(view.title);
            _primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MyWallet");
            alert.setHeaderText("Error");
            alert.setContentText("View load filed.");
            alert.showAndWait();
        }
    }

    public enum Views{
        LOGIN("login", "login", "Login to MyWallet"),
        REGISTER("register", "register", "Create new MyWallet account"),
        DASHBOARD("dashboard", "dashboard", "MyWallet");

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