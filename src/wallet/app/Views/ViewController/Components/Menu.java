package wallet.app.Views.ViewController.Components;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wallet.app.Helpers.AuthorizationManager;
import wallet.app.Views.ViewsManager;

import java.util.HashMap;

public class Menu {

    public static void registerMenu(VBox menuBar){
        HashMap<String, EventHandler> menuItemHandlerList = new HashMap<>();
        menuItemHandlerList.put("#dashboardBtn", event -> ViewsManager.loadView(ViewsManager.Views.DASHBOARD));
        menuItemHandlerList.put("#walletBtn", event -> ViewsManager.loadView(ViewsManager.Views.WALLET));
        menuItemHandlerList.put("#historyBtn", event -> ViewsManager.loadView(ViewsManager.Views.HISTORY));
        menuItemHandlerList.put("#logouttBtn", event -> {
            AuthorizationManager.logOut();
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        });

        Pane menuItem;
        for(HashMap.Entry item : menuItemHandlerList.entrySet()){
            menuItem = (Pane)menuBar.lookup(item.getKey().toString());
            if (menuItem != null){
                menuItem.addEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler)item.getValue());
            }
        }
    }

}
