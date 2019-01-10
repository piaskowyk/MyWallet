package wallet.Server.Controllers;

import com.google.gson.Gson;
import wallet.CommonElements.Entity.PaymentCategory;
import wallet.CommonElements.Entity.PaymentItem;
import wallet.CommonElements.Entity.User;
import wallet.CommonElements.Forms.PaymentForm;
import wallet.CommonElements.Forms.PaymentsHistoryForm;
import wallet.CommonElements.Forms.RemovePaymentsItemForm;
import wallet.Server.Exceptions.InvalidInputDataException;
import wallet.Server.Untils.AuthorizationUserManager;
import wallet.Server.Untils.DataBase;
import wallet.CommonElements.Responses.DataResponses.PaymentsHistoryResponse;
import wallet.CommonElements.Responses.DataResponses.StandardResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            boolean operationInit;

            StringBuilder query = new StringBuilder();

            query.append("select * from payments where user_id = ?");

            if(paymentsHistoryForm.getPaymentCategory() != null){
                query.append(" and category = '");
                query.append(paymentsHistoryForm.getPaymentCategory().getName());
                query.append("' ");
            }

            if(paymentsHistoryForm.getDateStart() != null){
                query.append(" and ");
                query.append(" date >= ");
                java.util.Date utilDate = paymentsHistoryForm.getDateStart();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                query.append("'");
                query.append(sqlDate);
                query.append("'");
            }

            if(paymentsHistoryForm.getDateEnd() != null){
                query.append(" and ");
                query.append(" date <= ");
                java.util.Date utilDate = paymentsHistoryForm.getDateEnd();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                query.append("'");
                query.append(sqlDate);
                query.append("'");
            }

            operationInit = false;
            if(paymentsHistoryForm.getFilterDateSort() != null){
                operationInit = true;
                query.append(" order by ");
                query.append("date ");
                query.append(paymentsHistoryForm.getFilterDateSort().getName());
                query.append(", id ");
                query.append(paymentsHistoryForm.getFilterDateSort().getName());
            }

            if(paymentsHistoryForm.getFilterAmountSort() != null){
                if(!operationInit){
                    query.append(" order by ");
                } else {
                    query.append(" ,");
                }
                query.append("amount ");
                query.append(paymentsHistoryForm.getFilterAmountSort().getName());
            }

            User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));
            System.out.println(headers.get("Auth-Token:"));
            if(user != null){
                arguments.add(user.getId());

                System.out.println(query.toString());
                ResultSet dbResult = db.querySelect(query.toString(), arguments);

                while (dbResult.next()){
                    PaymentItem paymentItem = new PaymentItem();
                    paymentItem.setId(dbResult.getInt("id"));
                    paymentItem.setAmount(dbResult.getFloat("amount"));
                    paymentItem.setTitle(dbResult.getString("title"));
                    paymentItem.setType(dbResult.getString("type"));
                    paymentItem.setCategory(dbResult.getString("category"));

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
                arguments.add(paymentForm.getDate());
                arguments.add(paymentForm.getCategory());
                arguments.add(paymentForm.getId());
                arguments.add(user.getId());

                db.queryUpdate("UPDATE payments SET amount = ?, title = ?, date = ?, category = ? WHERE id = ? and user_id = ?", arguments);
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
