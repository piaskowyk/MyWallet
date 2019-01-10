package wallet.Server.Controllers;

import com.google.gson.Gson;
import wallet.CommonElements.Entity.User;
import wallet.CommonElements.Forms.PaymentForm;
import wallet.CommonElements.Untils.Validator;
import wallet.Server.Exceptions.InvalidInputDataException;
import wallet.Server.Untils.AuthorizationUserManager;
import wallet.Server.Untils.DataBase;
import wallet.CommonElements.Responses.DataResponses.StandardResult;

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
