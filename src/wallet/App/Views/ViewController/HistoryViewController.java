package wallet.App.Views.ViewController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import wallet.App.Untils.AuthorizationManager;
import wallet.App.Views.IViewController;
import wallet.App.Views.ViewController.Components.Menu;
import wallet.App.Views.ViewController.ThreadsActions.LoadPaymentHistoryThread;
import wallet.CommonElements.Entity.PaymentCategory;
import wallet.CommonElements.Forms.PaymentsHistoryForm;

import java.util.EnumSet;

public class HistoryViewController implements IViewController {

    private String imageBasePathForTeared = "../../src/img/";

    @FXML
    private VBox historyContainer;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox menuBar;

    @FXML
    private ChoiceBox<String> filterDateSort, filterAmountSort, filterCategory;

    @FXML
    private DatePicker filterDateStart, filterDateEnd;

    @FXML
    private Button filterBtn;

    public void initialize(){
        Menu.registerMenu(menuBar);

        EnumSet.allOf(PaymentsHistoryForm.FilterDateSort.class)
                .forEach(item -> {
                    filterDateSort.getItems().add(item.getName());
                });
        filterDateSort.getSelectionModel().selectFirst();

        EnumSet.allOf(PaymentsHistoryForm.FilterAmountSort.class)
                .forEach(item -> {
                    filterAmountSort.getItems().add(item.getName());
                });
        filterAmountSort.getSelectionModel().selectFirst();

        EnumSet.allOf(PaymentCategory.class)
                .forEach(item -> {
                    filterCategory.getItems().add(item.getName());
                });
        filterCategory.getSelectionModel().selectFirst();

        filterBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            loadData();
        });

        if(AuthorizationManager.isAuthorized()){
            loadData();
        }
    }

    @Override
    public void onLoad() {
        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
        Thread getData = new Thread(loadPaymentHistoryThread);
        Platform.runLater(getData);
    }

    public String getImageBasePathForTeared() {
        return imageBasePathForTeared;
    }

    public VBox getHistoryContainer() {
        return historyContainer;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public ChoiceBox<String> getFilterDateSort() {
        return filterDateSort;
    }

    public ChoiceBox<String> getFilterAmountSort() {
        return filterAmountSort;
    }

    public ChoiceBox<String> getFilterCategory() {
        return filterCategory;
    }

    public DatePicker getFilterDateStart() {
        return filterDateStart;
    }

    public DatePicker getFilterDateEnd() {
        return filterDateEnd;
    }

    private void loadData(){
        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(this);
        Thread getData = new Thread(loadPaymentHistoryThread);
        Platform.runLater(getData);
    }

}
