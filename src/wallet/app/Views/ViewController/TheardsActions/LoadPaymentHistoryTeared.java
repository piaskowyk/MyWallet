package wallet.app.Views.ViewController.TheardsActions;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Pair;
import wallet.app.Exceptions.UnauthorizationRequestException;
import wallet.app.Helpers.Postman;
import wallet.app.Views.ViewController.HistoryViewController;
import wallet.server.Forms.PaymentForm;
import wallet.server.Forms.RemovePaymentsItemForm;
import wallet.server.Responses.DataResponses.PaymentsHistoryResponse;
import wallet.server.Responses.DataResponses.StandardResult;

import java.util.Optional;

public class LoadPaymentHistoryTeared implements Runnable{

    HistoryViewController controller;

    public LoadPaymentHistoryTeared(HistoryViewController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.getStatusLabel().setText("Waiting...");

        Postman<PaymentsHistoryResponse> postman = new Postman<PaymentsHistoryResponse>();
        PaymentsHistoryResponse paymentsHistoryResponse = null;
        try {
            paymentsHistoryResponse = postman.get(PaymentsHistoryResponse.class, Postman.Api.GET_HISTORY);
        } catch (UnauthorizationRequestException e) {
            controller.getStatusLabel().setText("You don't have permission to this operation.");
        }

        if(paymentsHistoryResponse.getStatus()){
            controller.getStatusLabel().setText("OK.");
        }
        else {
            controller.getStatusLabel().setText("Somethings went wrong.");
        }

        controller.getHistoryContainer().getChildren().clear();

        Integer index = 0;
        for(PaymentForm paymentItem : paymentsHistoryResponse.getPaymentsHistory()) {

            //main container list
            BorderPane borderPaneRoot = new BorderPane();
            if(index % 2 == 0) borderPaneRoot.setStyle("-fx-background-color: #F3F3F3;");
            borderPaneRoot.setMinHeight(90);

            //left part of item list
            Pane paneLeft = new Pane();
            paneLeft.setPrefWidth(100);
            ImageView icon = new ImageView();
            String iconName;
            if(paymentItem.getType() == PaymentForm.Type.INCOMING){
                iconName = "plus";
            } else {
                iconName = "minus";
            }
            Image iconImg = new Image(getClass().getResource(controller.getImageBasePathForTeared() + iconName + ".png").toExternalForm());

            icon.setImage(iconImg);
            icon.setLayoutX(14);
            icon.setLayoutY(30);
            Label title = new Label();
            title.setText(paymentItem.getTitle());
            title.setFont(Font.font ("Ubuntu", 14));
            title.setLayoutX(54);
            title.setLayoutY(38);
            paneLeft.getChildren().add(icon);
            paneLeft.getChildren().add(title);
            borderPaneRoot.setLeft(paneLeft);

            //middle part of list
            BorderPane borderPaneCenter = new BorderPane();
            Label priceLabel = new Label();
            String amountText;
            if(paymentItem.getType() == PaymentForm.Type.INCOMING){
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

                    Postman<StandardResult> postmanRemove = new Postman<StandardResult>();
                    StandardResult resultRemove = null;
                    RemovePaymentsItemForm removePaymentsItemForm = new RemovePaymentsItemForm();
                    removePaymentsItemForm.setId(Integer.parseInt(((Button)event.getSource()).getId()));
                    try {
                        resultRemove = postmanRemove.send(removePaymentsItemForm, StandardResult.class, Postman.Api.REMOVE_PAYMENTS_ITEM);
                    } catch (UnauthorizationRequestException e) {
                        controller.getStatusLabel().setText("You don't have permission to this operation.");
                    }

                    if(resultRemove.getStatus()){
                        controller.getStatusLabel().setText("OK.");
                        LoadPaymentHistoryTeared loadPaymentHistoryTeared = new LoadPaymentHistoryTeared(controller);
                        Thread getData = new Thread(loadPaymentHistoryTeared);
                        Platform.runLater(getData);
                    }
                    else {
                        controller.getStatusLabel().setText("Somethings went wrong.");
                    }
                }

            });

            btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                Dialog<Pair<String, String>> dialog = new Dialog<>();
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

                grid.add(new Label("Title:"), 0, 0);
                grid.add(paymentItemTitle, 1, 0);
                grid.add(new Label("Amount:"), 0, 1);
                grid.add(paymentItemAmount, 1, 1);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == editButton) {
                        return new Pair<>(paymentItemTitle.getText(), paymentItemAmount.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(item -> {
                    controller.getStatusLabel().setText("Wait... Delete item is in progress.");

                    Postman<StandardResult> postmanEdit = new Postman<StandardResult>();
                    StandardResult resultEdit = null;
                    PaymentForm paymentEditForm = new PaymentForm();
                    paymentEditForm.setId(Integer.parseInt(((Button)event.getSource()).getId()));
                    paymentEditForm.setTitle(item.getKey());
                    paymentEditForm.setAmount(Float.parseFloat(item.getValue()));
                    try {
                        resultEdit = postmanEdit.send(paymentEditForm, StandardResult.class, Postman.Api.EDIT_PAYMENTS_ITEM);
                    } catch (UnauthorizationRequestException e) {
                        controller.getStatusLabel().setText("You don't have permission to this operation.");
                    }

                    if(resultEdit.getStatus()){
                        controller.getStatusLabel().setText("OK.");
                        LoadPaymentHistoryTeared loadPaymentHistoryTeared = new LoadPaymentHistoryTeared(controller);
                        Thread getData = new Thread(loadPaymentHistoryTeared);
                        Platform.runLater(getData);
                    }
                    else {
                        controller.getStatusLabel().setText("Somethings went wrong.");
                    }
                });

            });


            controller.getHistoryContainer().getChildren().add(borderPaneRoot);
            index++;
        }
    }

}
