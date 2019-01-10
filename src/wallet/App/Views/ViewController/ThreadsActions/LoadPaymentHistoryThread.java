package wallet.App.Views.ViewController.ThreadsActions;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import wallet.App.Untils.Postman;
import wallet.App.Views.ViewController.HistoryViewController;
import wallet.CommonElements.Entity.PaymentCategory;
import wallet.CommonElements.Entity.PaymentItem;
import wallet.CommonElements.Forms.PaymentForm;
import wallet.CommonElements.Forms.PaymentsHistoryForm;
import wallet.CommonElements.Forms.RemovePaymentsItemForm;
import wallet.CommonElements.Responses.DataResponses.PaymentsHistoryResponse;
import wallet.CommonElements.Responses.DataResponses.StandardResult;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class LoadPaymentHistoryThread implements Runnable {

    private HistoryViewController controller;

    public LoadPaymentHistoryThread(HistoryViewController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.getStatusLabel().setText("Waiting...");

        LocalDate localDate;
        Instant instant;
        Date date;
        String filterValue;

        PaymentsHistoryForm paymentsHistoryForm = new PaymentsHistoryForm();

        filterValue = controller.getFilterDateSort().getValue();
        paymentsHistoryForm.setFilterDateSort(!filterValue.equals("") ?
                PaymentsHistoryForm.FilterDateSort.valueOf(controller.getFilterDateSort().getValue()) : null);

        filterValue = controller.getFilterAmountSort().getValue();
        paymentsHistoryForm.setFilterAmountSort(!filterValue.equals("") ?
                PaymentsHistoryForm.FilterAmountSort.valueOf(controller.getFilterAmountSort().getValue()) : null);

        filterValue = controller.getFilterCategory().getValue();
        paymentsHistoryForm.setPaymentCategory(!filterValue.equals("") ?
                PaymentCategory.valueOf(controller.getFilterCategory().getValue()) : null);

        date = null;
        localDate = controller.getFilterDateStart().getValue();
        if(localDate != null){
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
        }
        paymentsHistoryForm.setDateStart(date);

        date = null;
        localDate = controller.getFilterDateEnd().getValue();
        if(localDate != null){
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
        }
        paymentsHistoryForm.setDateEnd(date);


        Postman<PaymentsHistoryResponse> postman = new Postman<>();
        PaymentsHistoryResponse paymentsHistoryResponse = postman.send(paymentsHistoryForm, PaymentsHistoryResponse.class, Postman.Api.GET_HISTORY);

        if(postman.noError() && paymentsHistoryResponse.getStatus()) {
            controller.getStatusLabel().setText("OK.");
        }
        else {
            controller.getStatusLabel().setText(postman.getErrorMessage());
        }

        controller.getHistoryContainer().getChildren().clear();

        int index = 0;
        for(PaymentItem paymentItem : paymentsHistoryResponse.getPaymentsHistory()) {

            //main container list
            BorderPane borderPaneRoot = new BorderPane();
            if(index % 2 == 0) borderPaneRoot.setStyle("-fx-background-color: #d6d6d6;");
            borderPaneRoot.setMinHeight(90);

            //left part of item list
            Pane paneLeft = new Pane();
            paneLeft.setPrefWidth(100);
            ImageView icon = new ImageView();
            String iconName;
            if(paymentItem.getType() == PaymentItem.Type.INCOMING){
                iconName = "plus";
            } else {
                iconName = "minus";
            }
            Image iconImg = new Image(getClass().getResource(controller.getImageBasePathForTeared() + iconName + ".png").toExternalForm());

            icon.setImage(iconImg);
            icon.setLayoutX(14);
            icon.setLayoutY(30);
            Label dataLabel = new Label();
            dataLabel.setLayoutX(54);
            dataLabel.setLayoutY(28);
            dataLabel.setText(paymentItem.getDate());
            Label title = new Label();
            title.setText(paymentItem.getTitle());
            title.setFont(Font.font ("Ubuntu", 14));
            title.setLayoutX(54);
            title.setLayoutY(47);
            paneLeft.getChildren().add(icon);
            paneLeft.getChildren().add(title);
            paneLeft.getChildren().add(dataLabel);
            if(paymentItem.getCategory() != PaymentCategory.IN){
                Label categoryLabel = new Label();
                categoryLabel.setLayoutX(130);
                categoryLabel.setLayoutY(28);
                categoryLabel.setText("-" + paymentItem.getCategory().getName());
                paneLeft.getChildren().add(categoryLabel);
            }
            borderPaneRoot.setLeft(paneLeft);

            //middle part of list
            BorderPane borderPaneCenter = new BorderPane();
            Label priceLabel = new Label();
            String amountText;
            if(paymentItem.getType() == PaymentItem.Type.INCOMING){
                amountText = "+ " + paymentItem.getAmount().toString() + " zł";
            } else {
                amountText = "- " + paymentItem.getAmount().toString() + " zł";
            }
            priceLabel.setText(amountText);
            priceLabel.setAlignment(Pos.CENTER);
            borderPaneCenter.setCenter(priceLabel);
            borderPaneRoot.setCenter(borderPaneCenter);

            //right part of list
            Pane paneRight = new Pane();
            paneRight.setPrefWidth(227);
            Button btnRemove = new Button();
            btnRemove.setText("Remove");
            btnRemove.setLayoutX(30);
            btnRemove.setLayoutY(33);
            btnRemove.setPrefWidth(72);
            btnRemove.setId(paymentItem.getId().toString());
            Button btnEdit = new Button();
            btnEdit.setText("Edit");
            btnEdit.setLayoutX(124);
            btnEdit.setLayoutY(33);
            btnEdit.setPrefWidth(72);
            btnEdit.setId(paymentItem.getId().toString());
            paneRight.getChildren().add(btnRemove);
            paneRight.getChildren().add(btnEdit);
            borderPaneRoot.setRight(paneRight);

            btnRemove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation deleting");
                alert.setHeaderText("Ate you sure to remove this payment item?");
                alert.setContentText("If not, click Cancel");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                   controller.getStatusLabel().setText("Wait... Delete item is in progress.");

                    Postman<StandardResult> postmanRemove = new Postman<>();
                    RemovePaymentsItemForm removePaymentsItemForm = new RemovePaymentsItemForm();
                    removePaymentsItemForm.setId(Integer.parseInt(((Button)event.getSource()).getId()));
                    StandardResult resultRemove = postmanRemove.send(removePaymentsItemForm, StandardResult.class, Postman.Api.REMOVE_PAYMENTS_ITEM);

                    if(postmanRemove.noError() && resultRemove.getStatus()){
                        controller.getStatusLabel().setText("OK.");
                        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(controller);
                        Thread getData = new Thread(loadPaymentHistoryThread);
                        Platform.runLater(getData);
                    }
                    else {
                        controller.getStatusLabel().setText(postmanRemove.getErrorMessage());
                    }
                }
            });

            btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                Dialog<HashMap<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Edit payments information");
                dialog.setHeaderText("Edit payments information");


                ButtonType editButton = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(editButton, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField paymentItemTitle = new TextField();
                paymentItemTitle.setText(paymentItem.getTitle());

                TextField paymentItemAmount = new TextField();
                paymentItemAmount.setText(paymentItem.getAmount().toString());

                DatePicker paymentItemDate = new DatePicker();
                paymentItemDate.setValue(LocalDate.parse(paymentItem.getDate()));

                ChoiceBox<PaymentCategory> paymentItemCategory = new ChoiceBox<>();
                EnumSet.allOf(PaymentCategory.class)
                        .forEach(item -> {
                            if(item != PaymentCategory.IN){
                                paymentItemCategory.getItems().add(item);
                            }
                        });
                paymentItemCategory.getSelectionModel().select(paymentItem.getCategory());

                grid.add(new Label("Title:"), 0, 0);
                grid.add(paymentItemTitle, 1, 0);
                grid.add(new Label("Amount:"), 0, 1);
                grid.add(paymentItemAmount, 1, 1);

                grid.add(new Label("Date:"), 0, 2);
                grid.add(paymentItemDate, 1, 2);

                Label catLabel = new Label("Category:");
                grid.add(catLabel, 0, 3);
                grid.add(paymentItemCategory, 1, 3);

                if(paymentItem.getCategory() == PaymentCategory.IN){
                    catLabel.setVisible(false);
                    paymentItemCategory.setVisible(false);
                }


                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == editButton) {
                        HashMap<String, String> editForm = new HashMap<>();
                        editForm.put("title", paymentItemTitle.getText());
                        editForm.put("amount", paymentItemAmount.getText());
                        editForm.put("date", paymentItemDate.getValue().toString());
                        editForm.put("category", paymentItemCategory.getValue().toString());
                        return editForm;
                    }
                    return null;
                });

                Optional<HashMap<String, String>> result = dialog.showAndWait();

                result.ifPresent(item -> {
                    controller.getStatusLabel().setText("Wait... Delete item is in progress.");

                    Postman<StandardResult> postmanEdit = new Postman<>();
                    PaymentForm paymentEditForm = new PaymentForm();
                    paymentEditForm.setId(Integer.parseInt(((Button)event.getSource()).getId()));
                    paymentEditForm.setTitle(item.get("title"));
                    paymentEditForm.setAmount(Float.parseFloat(item.get("amount")));
                    paymentEditForm.setCategory(item.get("category"));
                    paymentEditForm.setDate(item.get("date"));
                    StandardResult resultEdit = postmanEdit.send(paymentEditForm, StandardResult.class, Postman.Api.EDIT_PAYMENTS_ITEM);

                    if(postmanEdit.noError() && resultEdit.getStatus()){
                        controller.getStatusLabel().setText("OK.");
                        LoadPaymentHistoryThread loadPaymentHistoryThread = new LoadPaymentHistoryThread(controller);
                        Thread getData = new Thread(loadPaymentHistoryThread);
                        Platform.runLater(getData);
                    }
                    else {
                        controller.getStatusLabel().setText(postmanEdit.getErrorMessage());
                    }
                });

            });

            controller.getHistoryContainer().getChildren().add(borderPaneRoot);
            index++;
        }

        if(index == 0){
            controller.getHistoryContainer().getChildren().clear();
            BorderPane borderPaneRoot = new BorderPane();
            Label info = new Label();
            info.setText("Not found payments history.");
            info.setFont(Font.font ("Ubuntu", 25));
            info.setStyle("-fx-padding: 50 0 0 0;");
            borderPaneRoot.setCenter(info);
            controller.getHistoryContainer().getChildren().add(borderPaneRoot);
        }

    }

}
