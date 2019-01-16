package wallet.server.controllers;

/*
 * use library:
 * javafx: https://github.com/javafxports/openjdk-jfx
 * gson: https://github.com/google/gson
 * */

import com.google.gson.Gson;
import javafx.util.Pair;
import wallet.commonElements.entity.User;
import wallet.commonElements.forms.DashboardForm;
import wallet.server.untils.AuthorizationUserManager;
import wallet.server.untils.DataBase;
import wallet.commonElements.responses.dataResponses.DashboardDataResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardController extends Controller {

    private HashMap<String, String> headers;
    private DataBase db = new DataBase();
    private User user = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    public DashboardDataResponse dashboard_dataAction(String json){
        DashboardDataResponse result = new DashboardDataResponse();
        DashboardForm dashboardForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            dashboardForm = gson.fromJson(json, DashboardForm.class);
            boolean operation = true;

            user = AuthorizationUserManager.isLogged(new DataBase(), headers.get("Auth-Token:"));

            if(user != null){
                if(dashboardForm.isStandardMode()){
                    result = generateDefaultMode();
                }
                else{
                    result = generateFilteredMode(dashboardForm);
                }
            } else {
                operation = false;
            }

            result.setStatus(operation);
        }
        catch (Exception e){
            e.printStackTrace();
            result.setStatus(false);
        }
        finally {
            db.closeConnection();
        }

        return result;
    }

    private DashboardDataResponse generateDefaultMode() throws SQLException {
        DashboardDataResponse result = new DashboardDataResponse();

        result.setAccountStateDuringMonth(getAccountStateDuringThisMonth());
        result.setIncomingState(getIncomingInThisMonth());
        result.setOutgoingState(getOutcomingInThisMonth());
        result.setAccountState(getAccountState());

        return result;
    }

    private DashboardDataResponse generateFilteredMode(DashboardForm dashboardForm) throws SQLException {
        DashboardDataResponse result = new DashboardDataResponse();

        result.setAccountStateDuringMonth(getAccountStateWithFilter(dashboardForm));
        result.setIncomingState(getIncomingInTimePeriod(dashboardForm));
        result.setOutgoingState(getOutcomingInTimePeriod(dashboardForm));
        result.setAccountState(getAccountState());
        result.setStandardMode(false);

        return result;
    }

    private float getIncomingInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        ResultSet dbResult;
        float incomingSum = 0;

        dbResult = db.querySelect("select sum(amount) incomingSum\n" +
                "from payments\n" +
                "where user_id = ?\n" +
                "  and year(date) = year(now())\n" +
                "  and month(date) = month(now())\n" +
                "  and type = 'INCOMING';", arguments);

        if(dbResult.next()){
            incomingSum = dbResult.getFloat("incomingSum");
        }

        return incomingSum;
    }

    private float getIncomingInTimePeriod(DashboardForm dashboardForm) throws SQLException {
        ResultSet dbResult;
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        float incomingSum = 0;

        String query = "select sum(amount) incomingSum\n" +
                "from payments\n" +
                "where user_id = ?\n" +
                "  {%dateMin%} {%dateMax%}\n" +
                "  and type = 'INCOMING';";

        if(dashboardForm.getDateStart() != null){
            arguments.add(formatter.format(dashboardForm.getDateStart()));
            query = query.replace("{%dateMin%}", "and date >= ?");
        }
        else {
            query = query.replace("{%dateMin%}", "");
        }

        if(dashboardForm.getDateEnd() != null){
            arguments.add(formatter.format(dashboardForm.getDateEnd()));
            query = query.replace("{%dateMax%}", "and date <= ?");
        }
        else {
            query = query.replace("{%dateMax%}", "");
        }

        dbResult = db.querySelect(query, arguments);

        if(dbResult.next()){
            incomingSum = dbResult.getFloat("incomingSum");
        }

        return incomingSum;
    }

    private float getOutcomingInTimePeriod(DashboardForm dashboardForm) throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        ResultSet dbResult;
        float incomingSum = 0;
        String query = "select sum(amount) incomingSum\n" +
                "from payments\n" +
                "where user_id = ?\n" +
                "  {%dateMin%} {%dateMax%}\n" +
                "  and type = 'OUTCOMING';";

        if(dashboardForm.getDateStart() != null){
            arguments.add(formatter.format(dashboardForm.getDateStart()));
            query = query.replace("{%dateMin%}", "and date >= ?");
        }
        else {
            query = query.replace("{%dateMin%}", "");
        }

        if(dashboardForm.getDateEnd() != null){
            arguments.add(formatter.format(dashboardForm.getDateEnd()));
            query = query.replace("{%dateMax%}", "and date <= ?");
        }
        else {
            query = query.replace("{%dateMax%}", "");
        }

        dbResult = db.querySelect(query, arguments);

        if(dbResult.next()){
            incomingSum = dbResult.getFloat("incomingSum");
        }

        return incomingSum;
    }

    private float getOutcomingInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        ResultSet dbResult;
        float outcomingSum = 0;

        dbResult = db.querySelect("select sum(amount) outcomingSum\n" +
                "from payments\n" +
                "where user_id = ?\n" +
                "  and year(date) = year(now())\n" +
                "  and month(date) = month(now())\n" +
                "  and type = 'OUTCOMING';", arguments);

        if(dbResult.next()){
            outcomingSum = dbResult.getFloat("outcomingSum");
        }

        return outcomingSum;
    }

    private float getAccountState() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        arguments.add(user.getId());

        ResultSet dbResult;
        float accountState = 0;

        dbResult = db.querySelect("select (select sum(amount)\n" +
                "        from payments\n" +
                "        where user_id = ?\n" +
                "          and date <= now()\n" +
                "          and type = 'INCOMING') -\n" +
                "       (select sum(amount)\n" +
                "        from payments\n" +
                "        where user_id = ?\n" +
                "          and date <= now()\n" +
                "          and type = 'OUTCOMING') accountState", arguments);

        if(dbResult.next()){
            accountState = dbResult.getFloat("accountState");
        }

        return accountState;
    }

    private float getLastMonthAccountState() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        arguments.add(user.getId());
        ResultSet dbResult;
        float lastMonthAccountState = 0;

        dbResult = db.querySelect("select (\n" +
                "         (\n" +
                "          ifnull((select sum(amount)\n" +
                "                 from payments\n" +
                "                 where user_id = ?\n" +
                "                   and (\n" +
                "                           year(date) <> year(now())\n" +
                "                           or month(date) <> month(now())\n" +
                "                         )\n" +
                "                   and type = 'INCOMING'), 0)\n" +
                "         )\n" +
                "         -\n" +
                "       (\n" +
                "         ifnull((select sum(amount)\n" +
                "                 from payments\n" +
                "                 where user_id = ?\n" +
                "                   and (\n" +
                "                     year(date) <> year(now())\n" +
                "                     or month(date) <> month(now())\n" +
                "                   )\n" +
                "                   and type = 'OUTCOMING'), 0)\n" +
                "         ) )\n" +
                "       accountSum\n" +
                "from payments p\n" +
                "where p.date <= DATE_FORMAT(NOW() ,'%Y-%m-01')\n" +
                "limit 1;", arguments);

        if(dbResult.next()){
            lastMonthAccountState = dbResult.getFloat("accountSum");
        }

        return lastMonthAccountState;
    }

    private HashMap<Date, Float> getAccountStateInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(user.getId());
        arguments.add(user.getId());
        arguments.add(user.getId());
        ResultSet dbResult;

        dbResult = db.querySelect("select (\n" +
                "           (\n" +
                "             ifnull((select sum(amount)\n" +
                "                     from payments\n" +
                "                     where user_id = ?\n" +
                "                       and year(date) = year(now())\n" +
                "                       and month(date) = month(now())\n" +
                "                       and day(date) = day(p.date)\n" +
                "                       and type = 'INCOMING'), 0)\n" +
                "             )\n" +
                "           -\n" +
                "           (\n" +
                "             ifnull((select sum(amount)\n" +
                "                     from payments\n" +
                "                     where user_id = ?\n" +
                "                       and year(date) = year(now())\n" +
                "                       and month(date) = month(now())\n" +
                "                       and day(date) = day(p.date)\n" +
                "                       and type = 'OUTCOMING'), 0)\n" +
                "             )\n" +
                "         )       accountSum,\n" +
                "       date day\n" +
                "from payments p\n" +
                "where p.date >= DATE_FORMAT(NOW() ,'%Y-%m-01') and day(p.date) <= day(now()) and user_id = ?\n" +
                "group by day(date);", arguments);

        HashMap<Date, Float> accountStateInThisMonth = new HashMap<>();
        while (dbResult.next()){
            accountStateInThisMonth.put(dbResult.getDate("day"), dbResult.getFloat("accountSum"));
        }

        return accountStateInThisMonth;
    }

    private HashMap<Date, Float> getAccountStateBettwenDate(Date dateStart, Date dateEnd) throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        ResultSet dbResult;

        String query = "select (\n" +
                "           (\n" +
                        "             ifnull((select sum(amount)\n" +
                        "                     from payments\n" +
                        "                     where user_id = ?\n" +
                        "                       {%dateMin%} {%dateMax%}\n" +
                        "                       and date = p.date\n" +
                        "                       and type = 'INCOMING'), 0)\n" +
                        "             )\n" +
                        "           -\n" +
                        "           (\n" +
                        "             ifnull((select sum(amount)\n" +
                        "                     from payments\n" +
                        "                     where user_id = ?\n" +
                        "                       {%dateMin%} {%dateMax%}\n" +
                        "                       and date = p.date\n" +
                        "                       and type = 'OUTCOMING'), 0)\n" +
                        "             )\n" +
                        "         )       accountSum,\n" +
                        "       date day\n" +
                        "from payments p\n" +
                        "where user_id = ?\n" +
                        "group by day(date);";

        if(dateStart != null && dateEnd != null){
            arguments.add(user.getId());
            arguments.add(formatter.format(dateStart));
            arguments.add(formatter.format(dateEnd));
            arguments.add(user.getId());
            arguments.add(formatter.format(dateStart));
            arguments.add(formatter.format(dateEnd));
            query = query.replace("{%dateMin%}", "and date >= ?");
            query = query.replace("{%dateMax%}", "and date <= ?");
        }
        else {
            if(dateStart != null){
                arguments.add(user.getId());
                arguments.add(formatter.format(dateStart));
                arguments.add(user.getId());
                arguments.add(formatter.format(dateStart));
                query = query.replace("{%dateMin%}", "and date >= ?");
            }
            else {
                query = query.replace("{%dateMin%}", "");
            }

            if(dateEnd != null){
                arguments.add(user.getId());
                arguments.add(formatter.format(dateEnd));
                arguments.add(user.getId());
                arguments.add(formatter.format(dateEnd));
                query = query.replace("{%dateMax%}", "and date <= ?");
            }
            else {
                query = query.replace("{%dateMax%}", "");
            }

            if(dateStart == null && dateEnd == null){
                arguments.add(user.getId());
                arguments.add(user.getId());
            }
        }
        arguments.add(user.getId());

        System.out.println(arguments.size());
        System.out.println(query);
        dbResult = db.querySelect(query, arguments);

        HashMap<Date, Float> accountStateBetweenDate = new HashMap<>();
        while (dbResult.next()){
            accountStateBetweenDate.put(dbResult.getDate("day"), dbResult.getFloat("accountSum"));
        }

        return accountStateBetweenDate;
    }

    private ArrayList<Pair<Date, Float>> getAccountStateDuringThisMonth() throws SQLException {
        float lastMonthAccountState = getLastMonthAccountState();
        ArrayList<Pair<Date, Float>> result = new ArrayList<>();

        HashMap<Date, Float> accountStateInThisMonth = getAccountStateInThisMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startMonth = calendar.getTime();

        float lastState = lastMonthAccountState;
        calendar.setTime(startMonth);
        for(int i = 1; i <= lastDay; i++){
            if(accountStateInThisMonth.containsKey(calendar.getTime())){
                lastState = lastMonthAccountState + accountStateInThisMonth.get(calendar.getTime());
            }

            result.add(new Pair<>(calendar.getTime(), lastState));
            calendar.add(Calendar.DATE, 1);
        }

        return result;
    }

    private ArrayList<Pair<Date, Float>> getAccountStateWithFilter(DashboardForm dashboardForm) throws SQLException {
        ArrayList<Pair<Date, Float>> result = new ArrayList<>();

        HashMap<Date, Float> accountStateInTimePeriod = getAccountStateBettwenDate(
                dashboardForm.getDateStart(),
                dashboardForm.getDateEnd());

        if(accountStateInTimePeriod.size() == 0){
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        Date minDate = new Date();
        Date maxDate = new Date();

        for(Date item : accountStateInTimePeriod.keySet()){
            System.out.println(item);
            if(item.compareTo(minDate) < 0) minDate = item;
            if(item.compareTo(maxDate) > 0) maxDate = item;
        }

        if(dashboardForm.getDateStart() == null){
            calendar.setTime(minDate);
        }
        else {
            calendar.setTime(dashboardForm.getDateStart());
        }

        if(dashboardForm.getDateEnd() != null){
            maxDate = dashboardForm.getDateEnd();
        }

        if(maxDate.compareTo(new Date()) > 0){
            maxDate = new Date();
        }

        float lastState = 0;

        while (calendar.getTime().compareTo(maxDate) <= 0){

            if(accountStateInTimePeriod.containsKey(calendar.getTime())){
                lastState += accountStateInTimePeriod.get(calendar.getTime());
                System.out.println(accountStateInTimePeriod.get(calendar.getTime()));
                System.out.println(lastState);
            }

            result.add(new Pair<>(calendar.getTime(), lastState));
            calendar.add(Calendar.DATE, 1);
        }

        return result;
    }

}
