package wallet.server.controllers;

/*
 * use library:
 * gson: https://github.com/google/gson
 * */

import com.google.gson.Gson;

import wallet.commonElements.entity.User;
import wallet.commonElements.forms.PaymentForm;
import wallet.commonElements.untils.Validator;
import wallet.server.exceptions.InvalidInputDataException;
import wallet.server.untils.AuthorizationUserManager;
import wallet.server.untils.DataBase;
import wallet.commonElements.responses.dataResponses.StandardResult;

import java.util.ArrayList;
import java.util.HashMap;

public class WalletController extends Controller {

    /*
     * Kontroler w którym obsługiwane akcje związane z dodawaniem płatności
     * */

    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    /*
     * Dodanie płatności przez użytkownika
     * */
    public StandardResult add_paymentAction(String json){
        StandardResult result = new StandardResult();
        PaymentForm paymentForms;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments = new ArrayList<>();
            paymentForms = gson.fromJson(json, PaymentForm.class);
            boolean operation = true;
            if(paymentForms.getAmount() < 0){
                operation = false;
            }
            else {
                User user = AuthorizationUserManager.isLogged(db, headers.get("Auth-Token:"));

                if(user != null){

                    //validation data
                    if(
                            paymentForms.getTitle().length() > 5000
                            || !Validator.isValidAplhaString(paymentForms.getType().toString())
                    ) {
                        throw new InvalidInputDataException();
                    }

                    arguments.add(user.getId());
                    arguments.add(paymentForms.getAmount());
                    arguments.add(paymentForms.getTitle());
                    arguments.add(paymentForms.getType());
                    arguments.add(paymentForms.getDate());
                    arguments.add(paymentForms.getCategory());

                    db.queryUpdate("insert into payments (user_id, amount, title, type, date, category) values (?, ?, ?, ?, ?, ?)", arguments);
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
