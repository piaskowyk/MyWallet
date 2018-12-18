package wallet.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    private boolean isLog = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!isLog){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
                BorderPane root = (BorderPane) loader.load();
                LoginViewController controller = loader.getController();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("views/login.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login");
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/main.fxml"));
                BorderPane root = (BorderPane) loader.load();
                MainViewController controller = loader.getController();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("views/style.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.setTitle("Hello World");
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
