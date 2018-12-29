package wallet.Server.Controllers;

import com.google.gson.Gson;
import wallet.CommonElements.Entity.User;
import wallet.CommonElements.Forms.DashboardForm;
import wallet.Server.Helpers.AuthorizationUserManager;
import wallet.Server.Helpers.DataBase;
import wallet.CommonElements.Responses.DataResponses.DashboardDataResponse;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class DashboardController extends Controller {

    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    public DashboardDataResponse dashboard_dataAction(String json){
        DashboardDataResponse result = new DashboardDataResponse();
        DashboardForm dashboardForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<>();
            dashboardForm = gson.fromJson(json, DashboardForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){
                ResultSet dbResult;
                float incomingSum = 0;
                float outcomingSum = 0;

                //in this month
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

                dbResult = db.querySelect("select sum(amount) outcomingSum\n" +
                        "from payments\n" +
                        "where user_id = ?\n" +
                        "  and year(date) = year(now())\n" +
                        "  and month(date) = month(now())\n" +
                        "  and type = 'OUTCOMING';", arguments);

                if(dbResult.next()){
                    outcomingSum = dbResult.getFloat("outcomingSum");
                }


                arguments = new ArrayList<>();
                arguments.add(user.getId());
                arguments.add(user.getId());

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
                        "from payments\n" +
                        "limit 1;", arguments);

                if(dbResult.next()){
                    lastMonthAccountState = dbResult.getFloat("accountSum");
                }

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
                        "group by day(date);", arguments);

                float lastState = lastMonthAccountState;
                int lastDay = 0;
                HashMap<Integer, Float> accountState = new HashMap<>();
                while (dbResult.next()){
                    if (lastDay < dbResult.getInt("day")) lastDay = dbResult.getInt("day");
                    accountState.put(dbResult.getInt("day"), lastMonthAccountState + dbResult.getFloat("accountSum"));
                }

                for(int i = 1; i <= lastDay; i++){
                    if(accountState.keySet().contains(i)){
                        lastState = lastMonthAccountState + accountState.get(i);
                    }
                    result.getAccountStateDuringMonth().put(i, lastState);
                }

                result.setIncomingState(incomingSum);
                result.setOutgoingState(outcomingSum);
                result.setAccountState(lastState);

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

}
