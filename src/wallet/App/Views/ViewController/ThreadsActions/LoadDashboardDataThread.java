package wallet.App.Views.ViewController.ThreadsActions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.*;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.PopupWindow;
import javafx.util.Pair;
import wallet.App.Untils.Postman;
import wallet.App.Views.ViewController.DashboardViewController;
import wallet.CommonElements.Entity.PaymentCategory;
import wallet.CommonElements.Forms.DashboardForm;
import wallet.CommonElements.Responses.DataResponses.DashboardDataResponse;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class LoadDashboardDataThread implements Runnable {

    private DashboardViewController controller;

    public LoadDashboardDataThread(DashboardViewController controller){
        this.controller = controller;
    }

    @Override
    public void run() {

        controller.getStatusLabel().setText("Waiting...");

        LocalDate localDate;
        Instant instant;
        Date date;

        DashboardForm dashboardForm = new DashboardForm();
        dashboardForm.setStandardMode(controller.getModeFilter().isSelected());

        ArrayList<PaymentCategory> paymentCategoriesList = new ArrayList<>();
        for(var item : controller.getShowCategoryFilter().getItems()){
            if(item instanceof CheckMenuItem && ((CheckMenuItem)item).isSelected()){
                paymentCategoriesList.add(PaymentCategory.valueOf(item.getText()));
            }
        }
        dashboardForm.setShowCategoriesList(paymentCategoriesList);

        date = null;
        localDate = controller.getDateStartFilter().getValue();
        if(localDate != null){
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
        }
        dashboardForm.setDateStart(date);

        date = null;
        localDate = controller.getDateEndFilter().getValue();
        if(localDate != null){
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
        }
        dashboardForm.setDateEnd(date);

        Postman<DashboardDataResponse> postman = new Postman<>();
        DashboardDataResponse dashboardDataResponse = postman.send(dashboardForm, DashboardDataResponse.class, Postman.Api.DASHBOARD_DATA);

        if(postman.noError() && dashboardDataResponse.getStatus()){
            controller.getStatusLabel().setText("OK.");
        }
        else {
            controller.getStatusLabel().setText(postman.getErrorMessage());
        }

        //set up labels
        DecimalFormat df = new DecimalFormat("0.00");
        controller.getAccountStateLabel().setText(df.format(dashboardDataResponse.getAccountState()) + " zł");
        controller.getIncomingStatusLabel().setText(df.format(dashboardDataResponse.getIncomingState()) + " zł");
        controller.getOutcomingStatusLabel().setText(df.format(dashboardDataResponse.getOutgoingState()) + " zł");

        //set up circle chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("incoming",
                    dashboardDataResponse.getIncomingState() / dashboardDataResponse.getAccountState()
            ),
            new PieChart.Data("outgoing",
                    dashboardDataResponse.getOutgoingState() / dashboardDataResponse.getAccountState()
            ));

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Incoming / outgoing");
        chart.setLabelsVisible(false);

        controller.getCircleChartContainer().setCenter(chart);

        int i = 0;
        for (PieChart.Data data : pieChartData) {
            if(i == 0) data.getNode().setStyle("-fx-pie-color: #37ef87;");
            else data.getNode().setStyle("-fx-pie-color: #ef3737;");
            i++;
        }

        //set up main chart
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        NumberAxis xAxis = new NumberAxis(1, dayInMonth, 1);
        NumberAxis yAxis = new NumberAxis();
        AreaChart<Number, Number> accountStateChart = new AreaChart<>(xAxis,yAxis);
        accountStateChart.setTitle("Account status history");
        accountStateChart.setLegendVisible(false);

        XYChart.Series<Integer, Float>  accountStateSeries = new XYChart.Series<>();

        for(Map.Entry<Integer, Float> item : dashboardDataResponse.getAccountStateDuringMonth().entrySet()){
            accountStateSeries.getData().add(new XYChart.Data<>(item.getKey(), item.getValue()));
        }

        accountStateChart.getData().addAll((XYChart.Series)accountStateSeries);

        for (XYChart.Data<Integer, Float> data : accountStateSeries.getData()) {
            Tooltip tooltip = new Tooltip("Day: " + data.getXValue().toString() + "\n" + "Account state : " + data.getYValue() + " zł");
            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
            data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
        }

        for (XYChart.Data<Integer, Float> data : accountStateSeries.getData()) {
            data.getNode().setOnMouseEntered(e -> {
                controller.getStatusLabel().setText("Day: " + data.getXValue().toString() + ", Account state : " + data.getYValue() + " zł");
            });
            data.getNode().setOnMouseExited(event -> {
                data.getNode().getStyleClass().remove("onHover");
                controller.getStatusLabel().setText("");
            });
        }

        controller.getAreaChartContainer().setCenter(accountStateChart);

    }

}
