package wallet.App.Views.ViewController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import wallet.App.Untils.AuthorizationManager;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewController.Components.Menu;
import wallet.App.Views.ViewController.ThreadsActions.LoadDashboardDataThread;
import wallet.CommonElements.Entity.PaymentCategory;

import java.util.EnumSet;

public class DashboardViewController implements IViewController {

    @FXML
    private BorderPane areaChartContainer, circleChartContainer;

    @FXML
    private Label accountStateLabel, incomingStatusLabel, outcomingStatusLabel, statusLabel;

    @FXML
    private VBox menuBar;

    @FXML
    private CheckBox modeFilter;

    @FXML
    private MenuButton showCategoryFilter;

    @FXML
    private DatePicker dateStartFilter, dateEndFilter;

    @FXML
    private Button refreshBtn;

    public void initialize(){
        Menu.registerMenu(menuBar);

        if(AuthorizationManager.isAuthorized()){
            LoadDashboardDataThread loadDashboardDataThread = new LoadDashboardDataThread(this);
            Thread getData = new Thread(loadDashboardDataThread);
            Platform.runLater(getData);
        }

        EnumSet.allOf(PaymentCategory.class)
                .forEach(item -> {
                    showCategoryFilter.getItems().add(new CheckMenuItem(item.getName()));
                });

        setDisableFilter(true);

        refreshBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            LoadDashboardDataThread loadDashboardDataThread = new LoadDashboardDataThread(this);
            Thread getData = new Thread(loadDashboardDataThread);
            Platform.runLater(getData);
        });

        modeFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setDisableFilter(modeFilter.isSelected());
        });
    }

    private void setDisableFilter(boolean disable){
        showCategoryFilter.setDisable(disable);
        dateStartFilter.setDisable(disable);
        dateEndFilter.setDisable(disable);
    }

    @Override
    public void onLoad() {
        LoadDashboardDataThread loadDashboardDataThread = new LoadDashboardDataThread(this);
        Thread getData = new Thread(loadDashboardDataThread);
        Platform.runLater(getData);
    }

    public BorderPane getAreaChartContainer() {
        return areaChartContainer;
    }

    public BorderPane getCircleChartContainer() {
        return circleChartContainer;
    }

    public Label getAccountStateLabel() {
        return accountStateLabel;
    }

    public Label getIncomingStatusLabel() {
        return incomingStatusLabel;
    }

    public Label getOutcomingStatusLabel() {
        return outcomingStatusLabel;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public CheckBox getModeFilter() {
        return modeFilter;
    }

    public MenuButton getShowCategoryFilter() {
        return showCategoryFilter;
    }

    public DatePicker getDateStartFilter() {
        return dateStartFilter;
    }

    public DatePicker getDateEndFilter() {
        return dateEndFilter;
    }
}
