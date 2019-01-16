package wallet.app.views.viewController;

/*
 * use library:
 * javafx: https://github.com/javafxports/openjdk-jfx
 * */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import wallet.app.untils.AuthorizationManager;
import wallet.app.views.IViewController;
import wallet.app.views.viewController.components.Menu;
import wallet.app.views.viewController.threadsActions.LoadDashboardDataThread;

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

    public DatePicker getDateStartFilter() {
        return dateStartFilter;
    }

    public DatePicker getDateEndFilter() {
        return dateEndFilter;
    }
}
