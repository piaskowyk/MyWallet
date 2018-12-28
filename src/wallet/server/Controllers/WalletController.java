package wallet.server.Controllers;

import com.google.gson.Gson;
import wallet.server.Entity.User;
import wallet.server.Forms.PaymentForm;
import wallet.server.Helpers.AuthorizationUserManager;
import wallet.server.Helpers.DataBase;
import wallet.server.Responses.DataResponses.StandardResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class WalletController extends Controller {

    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    public StandardResult add_paymentAction(String json){
        StandardResult result = new StandardResult();
        PaymentForm paymentForms;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<>();
            ResultSet dbResult;
            paymentForms = gson.fromJson(json, PaymentForm.class);
            boolean operation = true;
            if(paymentForms.getAmount() < 0){
                operation = false;
            }
            else {
                User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

                if(user != null){
                    arguments.add(user.getId());
                    arguments.add(paymentForms.getAmount());
                    arguments.add(paymentForms.getTitle());
                    arguments.add(paymentForms.getType());

                    db.queryUpdate("insert into payments (user_id, amount, title, type) values (?, ?, ?, ?)", arguments);
                } else {
                    operation = false;
                }
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
