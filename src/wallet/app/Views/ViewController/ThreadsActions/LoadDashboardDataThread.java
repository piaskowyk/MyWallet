package wallet.app.Views.ViewController.ThreadsActions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.util.Pair;
import wallet.app.Exceptions.UnauthorizationRequestException;
import wallet.app.Helpers.Postman;
import wallet.app.Views.ViewController.DashboardViewController;
import wallet.server.Responses.DataResponses.DashboardDataResponse;
import wallet.server.Responses.DataResponses.PaymentsHistoryResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class LoadDashboardDataThread implements Runnable {

    DashboardViewController controller;

    public LoadDashboardDataThread(DashboardViewController controller){
        this.controller = controller;
    }

    @Override
    public void run() {

        controller.getStatusLabel().setText("Waiting...");

        Postman<DashboardDataResponse> postman = new Postman<DashboardDataResponse>();
        DashboardDataResponse dashboardDataResponse = null;
        try {
            dashboardDataResponse = postman.get(DashboardDataResponse.class, Postman.Api.DASHBOARD_DATA);
        } catch (UnauthorizationRequestException e) {
            controller.getStatusLabel().setText("You don't have permission to this operation.");
        }

        if(dashboardDataResponse.getStatus()){
            controller.getStatusLabel().setText("OK.");
        }
        else {
            controller.getStatusLabel().setText("Somethings went wrong.");
        }

        //set up labels
        DecimalFormat df = new DecimalFormat(".00");
        controller.getAccountStateLabel().setText(df.format(dashboardDataResponse.getAccountState()) + " zł");
        controller.getIncomingStatusLabel().setText(df.format(dashboardDataResponse.getIncomingState()) + " zł");
        controller.getOutcomingStatusLabel().setText(df.format(dashboardDataResponse.getOutcomingState()) + " zł");

        //set up circle chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("incoming",
                    dashboardDataResponse.getIncomingState() / dashboardDataResponse.getAccountState()
            ),
            new PieChart.Data("outcoming",
                    dashboardDataResponse.getOutcomingState() / dashboardDataResponse.getAccountState()
            ));

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Incoming / outcoming");
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
        AreaChart<Number, Number> accountStateChart = new AreaChart<Number, Number>(xAxis,yAxis);
        accountStateChart.setTitle("Account status history");
        accountStateChart.setLegendVisible(false);

        XYChart.Series accountStateSeries = new XYChart.Series();
        //accountStateSeries.setName("Account status history");

        for(Map.Entry<Integer, Float> item : dashboardDataResponse.getAccountStateDuringMonth().entrySet()){
            accountStateSeries.getData().add(new XYChart.Data(item.getKey(), item.getValue()));
        }

        accountStateChart.getData().addAll(accountStateSeries);

        controller.getAreaChartContainer().setCenter(accountStateChart);

    }

}
