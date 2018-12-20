package wallet.app.views.ViewController;

import com.sun.javafx.fxml.builder.URLBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private String imageBasePath = "views/img/";

    @FXML
    private ImageView wallet;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Image img = new Image(getClass().getResource(imageBasePath + "wallet.png").toExternalForm());
        wallet.setImage(img);
    }
}
