package wallet.app;

import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {
    @FXML
    private ImageView mleko;

    public void tmpVoid(){
        Image img = new Image("wallet.png");
        mleko.setImage(img);
    }


}
