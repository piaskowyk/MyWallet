package wallet.app.views.viewController.components;

/*
 * use library:
 * javafx: https://github.com/javafxports/openjdk-jfx
 * */

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import wallet.app.untils.AuthorizationManager;
import wallet.app.views.ViewsManager;

import java.util.HashMap;

public class Menu {

    /*
    * Klasa odpowiedzalna jest za obługę onclicków na elementy menu
    * */

    public static void registerMenu(VBox menuBar){
        HashMap<String, EventHandler<MouseEvent>> menuItemHandlerList = new HashMap<>();
        menuItemHandlerList.put("#dashboardBtn", event -> ViewsManager.loadView(ViewsManager.Views.DASHBOARD));
        menuItemHandlerList.put("#walletBtn", event -> ViewsManager.loadView(ViewsManager.Views.WALLET));
        menuItemHandlerList.put("#historyBtn", event -> ViewsManager.loadView(ViewsManager.Views.HISTORY));
        menuItemHandlerList.put("#logoutBtn", event -> {
            AuthorizationManager.logOut();
            ViewsManager.loadView(ViewsManager.Views.LOGIN);
        });

        Pane menuItem;
        for(HashMap.Entry<String, EventHandler<MouseEvent>> item : menuItemHandlerList.entrySet()){
            menuItem = (Pane)menuBar.lookup(item.getKey());
            if (menuItem != null){
                menuItem.addEventHandler(MouseEvent.MOUSE_CLICKED, item.getValue());
            }
        }
    }

}
