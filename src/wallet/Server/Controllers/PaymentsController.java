package wallet.Server.Controllers;

import com.google.gson.Gson;
import wallet.CommonElements.Entity.PaymentItem;
import wallet.CommonElements.Entity.User;
import wallet.CommonElements.Forms.PaymentForm;
import wallet.CommonElements.Forms.PaymentsHistoryForm;
import wallet.CommonElements.Forms.RemovePaymentsItemForm;
import wallet.CommonElements.Helpers.Validator;
import wallet.Server.Exceptions.InvalidInputDataException;
import wallet.Server.Helpers.AuthorizationUserManager;
import wallet.Server.Helpers.DataBase;
import wallet.CommonElements.Responses.DataResponses.PaymentsHistoryResponse;
import wallet.CommonElements.Responses.DataResponses.StandardResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentsController extends Controller {

    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    public PaymentsHistoryResponse get_historyAction(String json){
        PaymentsHistoryResponse result = new PaymentsHistoryResponse();
        PaymentsHistoryForm paymentsHistoryForm;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<>();
            paymentsHistoryForm = gson.fromJson(json, PaymentsHistoryForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));
            System.out.println(headers.get("Auth-Token:"));
            if(user != null){
                arguments.add(user.getId());

                ResultSet dbResult = db.querySelect("select * from payments where user_id = ?", arguments);

                while (dbResult.next()){
                    PaymentItem paymentItem = new PaymentItem();
                    paymentItem.setId(dbResult.getInt("id"));
                    paymentItem.setAmount(dbResult.getFloat("amount"));
                    paymentItem.setTitle(dbResult.getString("title"));
                    paymentItem.setType(dbResult.getString("type"));

                    String dataStr = dbResult.getString("date");
                    if(dataStr.contains(".")){
                        dataStr = dataStr.substring(0, dataStr.indexOf("."));
                    }
                    paymentItem.setDate(dataStr);
                    result.getPaymentsHistory().add(paymentItem);
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
            ArrayList<Object> arguments = new ArrayList<>();
            removePaymentsItemForm = gson.fromJson(json, RemovePaymentsItemForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){
                arguments.add(removePaymentsItemForm.getId());
                arguments.add(user.getId());

                db.queryUpdate("delete from payments where id = ? and user_id = ?", arguments);
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
            ArrayList<Object> arguments = new ArrayList<>();
            paymentForm = gson.fromJson(json, PaymentForm.class);
            boolean operation = true;

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

            if(user != null){

                //validation data
                if(
                        paymentForm.getTitle().length() > 5000
                        || paymentForm.getAmount() < 0
                ) {
                    throw new InvalidInputDataException();
                }

                arguments.add(paymentForm.getAmount());
                arguments.add(paymentForm.getTitle());
                arguments.add(paymentForm.getId());
                arguments.add(user.getId());

                db.queryUpdate("UPDATE payments SET amount = ?, title = ? WHERE id = ? and user_id = ?", arguments);
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
