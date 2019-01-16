package wallet.app.views.viewController.threadsActions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.util.Pair;
import wallet.app.untils.Postman;
import wallet.app.views.viewController.DashboardViewController;
import wallet.commonElements.forms.DashboardForm;
import wallet.commonElements.responses.dataResponses.DashboardDataResponse;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

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

        NumberAxis xAxis;
        if(dashboardDataResponse.isStandardMode()){
            xAxis = new NumberAxis(1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 1);
        }
        else {
            xAxis = new NumberAxis();
        }

        NumberAxis yAxis = new NumberAxis();
        AreaChart<Number, Number> accountStateChart = new AreaChart<>(xAxis,yAxis);

        if(!dashboardDataResponse.isStandardMode()){
            accountStateChart.getXAxis().setVisible(false);
            accountStateChart.getXAxis().setOpacity(0);
        }

        accountStateChart.setTitle("Account status history");
        accountStateChart.setLegendVisible(false);

        XYChart.Series<Integer, Float>  accountStateSeries = new XYChart.Series<>();

        int index = 0;
        for(Pair<Date, Float> item : dashboardDataResponse.getAccountStateDuringMonth()){
            accountStateSeries.getData().add(new XYChart.Data<>(index, item.getValue()));
            index++;
        }

        accountStateChart.getData().addAll((XYChart.Series)accountStateSeries);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        index = 0;
        for (XYChart.Data<Integer, Float> data : accountStateSeries.getData()) {
            Tooltip tooltip = new Tooltip("Day: " + formatter.format(dashboardDataResponse.getAccountStateDuringMonth().get(index).getKey())
                    + "\n" + "Account state : " + data.getYValue() + " zł");
            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
            data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
            index++;
        }

        index = 0;
        for (XYChart.Data<Integer, Float> data : accountStateSeries.getData()) {
            String dateItem = formatter.format(dashboardDataResponse.getAccountStateDuringMonth().get(index).getKey());
            data.getNode().setOnMouseEntered(e -> {
                controller.getStatusLabel().setText("Day: " + dateItem + ", Account state : " + data.getYValue() + " zł");
            });
            data.getNode().setOnMouseExited(event -> {
                data.getNode().getStyleClass().remove("onHover");
                controller.getStatusLabel().setText("");
            });
            index++;
        }

        controller.getAreaChartContainer().setCenter(accountStateChart);

    }

}
