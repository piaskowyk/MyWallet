package wallet.server.Controllers;

import com.google.gson.Gson;
import wallet.server.Entity.User;
import wallet.server.Forms.PaymentForm;
import wallet.server.Forms.PaymentsHistoryForm;
import wallet.server.Forms.RemovePaymentsItemForm;
import wallet.server.Helpers.AuthorizationUserManager;
import wallet.server.Helpers.DataBase;
import wallet.server.Helpers.Validator;
import wallet.server.Responses.DataResponses.PaymentsHistoryResponse;
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
            ArrayList<Object> arguments = new ArrayList<Object>();;
            ResultSet dbResult;
            paymentForms = gson.fromJson(json, PaymentForm.class);
            boolean operation = true;
            if(!Validator.isValidFloatNum(paymentForms.getAmount().toString())){//TODO: sprawdzić to bo to dziwne jest
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

    public PaymentsHistoryResponse get_historyAction(String json){
        PaymentsHistoryResponse result = new PaymentsHistoryResponse();
        PaymentsHistoryForm paymentsHistoryForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<Object>();;
            paymentsHistoryForm = gson.fromJson(json, PaymentsHistoryForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){
                arguments.add(user.getId());

                ResultSet dbResult = db.querySelect("select * from payments where user_id = ?", arguments);

                while (dbResult.next()){
                    PaymentForm paymentForm = new PaymentForm();
                    paymentForm.setId(dbResult.getInt("id"));
                    paymentForm.setAmount(dbResult.getFloat("amount"));
                    paymentForm.setTitle(dbResult.getString("title"));
                    paymentForm.setType(dbResult.getString("type"));
                    result.getPaymentsHistory().add(paymentForm);
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

    public StandardResult remove_payments_itemAction(String json){
        StandardResult result = new StandardResult();
        RemovePaymentsItemForm removePaymentsItemForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<Object>();;
            removePaymentsItemForm = gson.fromJson(json, RemovePaymentsItemForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){
                arguments.add(removePaymentsItemForm.getId());

                db.queryUpdate("delete from payments where id = ?", arguments);
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

    public StandardResult edit_payments_itemAction(String json){
        StandardResult result = new StandardResult();
        PaymentForm paymentForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<Object>();;
            paymentForm = gson.fromJson(json, PaymentForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){
                arguments.add(paymentForm.getAmount());
                arguments.add(paymentForm.getTitle());
                arguments.add(paymentForm.getId());

                db.queryUpdate("UPDATE payments SET amount = ?, title = ? WHERE id = ?", arguments);
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
