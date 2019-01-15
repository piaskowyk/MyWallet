package wallet.Server.Controllers;

import com.google.gson.Gson;
import wallet.CommonElements.Entity.User;
import wallet.CommonElements.Forms.DashboardForm;
import wallet.Server.Untils.AuthorizationUserManager;
import wallet.Server.Untils.DataBase;
import wallet.CommonElements.Responses.DataResponses.DashboardDataResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DashboardController extends Controller {

    private HashMap<String, String> headers;
    private DataBase db = new DataBase();
    private User user = null;

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
                if(!dashboardForm.isStandardMode()){
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
        result.setIncomingState(getIncomingInThisMonth());
        result.setOutgoingState(getOutcomingInThisMonth());
        result.setAccountState(getAccountState());

        return result;
    }

    private float getIncomingInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
        ResultSet dbResult;
        float incomingSum = 0;

        arguments.add(user.getId());
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

    private float getOutcomingInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
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
                "        where user_id = 1\n" +
                "          and date <= now()\n" +
                "          and type = 'INCOMING') -\n" +
                "       (select sum(amount)\n" +
                "        from payments\n" +
                "        where user_id = 1\n" +
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

    private HashMap<Integer, Float> getAccountStateInThisMonth() throws SQLException {
        ArrayList<Object> arguments = new ArrayList<>();
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
                "       day(date) day\n" +
                "from payments p\n" +
                "where p.date >= DATE_FORMAT(NOW() ,'%Y-%m-01') and day(p.date) <= day(now())\n" +
                "group by day(date);", arguments);

        HashMap<Integer, Float> accountStateInThisMonth = new HashMap<>();
        while (dbResult.next()){
            accountStateInThisMonth.put(dbResult.getInt("day"), dbResult.getFloat("accountSum"));
        }

        return accountStateInThisMonth;
    }

    private HashMap<Integer, Float> getAccountStateDuringThisMonth() throws SQLException {
        float lastMonthAccountState = getLastMonthAccountState();
        HashMap<Integer, Float> result = new HashMap<>();
        HashMap<Integer, Float> accountStateInThisMonth = getAccountStateInThisMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        float lastState = lastMonthAccountState;
        for(int i = 1; i <= lastDay; i++){
            if(accountStateInThisMonth.keySet().contains(i)){
                lastState = lastMonthAccountState + accountStateInThisMonth.get(i);
            }
            result.put(i, lastState);
        }

        return result;
    }

    private HashMap<Integer, Float> getAccountStateWithFilter(DashboardForm dashboardForm){

    }

}
