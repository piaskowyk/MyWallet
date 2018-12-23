package wallet.server.Controllers;

import com.google.gson.Gson;
import wallet.server.Forms.Login;
import wallet.server.Forms.Register;
import wallet.server.Helpers.DataBase;
import wallet.server.Responses.DataResponses.StandardResult;
import wallet.server.Responses.DataResponses.LoginResponse;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserController extends Controller {

    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers){
        this.headers = headers;
    }

    public StandardResult registerAction(String json){
        StandardResult result = new StandardResult();
        Register registerForms;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments;
            ResultSet dbResult;
            registerForms = gson.fromJson(json, Register.class);
            boolean operation = true;

            arguments = new ArrayList<Object>();
            arguments.add(registerForms.getEmail());

            dbResult = db.querySelect("select * from users where email = ?", arguments);

            if(!db.exist(dbResult)){
                arguments = new ArrayList<Object>();
                arguments.add(registerForms.getName());
                arguments.add(registerForms.getSurname());
                arguments.add(registerForms.getEmail());
                arguments.add(registerForms.getPassword());

                db.queryUpdate("insert into users (name, surname, email, password) values (?, ?, ?, ?)", arguments);
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

    public LoginResponse loginAction(String json){
        LoginResponse result = new LoginResponse();
        Login login;
        DataBase db = new DataBase();

        try {
            Gson gson = new Gson();
            ArrayList<Object> arguments;
            ResultSet dbResult;
            login = gson.fromJson(json, Login.class);

            arguments = new ArrayList<Object>();
            arguments.add(login.getEmail());
            arguments.add(login.getPassword());

            dbResult = db.querySelect("select * from users where email = ? and password = ?", arguments);

            if (dbResult.next()){
                arguments = new ArrayList<Object>();

                //generate token for user
                Random random = new Random();
                StringBuilder token = new StringBuilder();
                String charSet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
                for(int i = 0; i < 100; i++){
                    token.append(charSet.charAt(random.nextInt(charSet.length())));
                }

                //calculate expire token date
                SimpleDateFormat dtFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                long time = calendar.getTimeInMillis();
                Date expireDate = new Date(time + (60 * 60000));
                String expireDateStr = dtFormatter.format(expireDate);

                arguments.add(token);
                arguments.add(expireDateStr);
                arguments.add(dbResult.getString("id"));

                db.queryUpdate("update users set token = ?, expired_date = ? where id = ?", arguments);
                result.setStatus(true);
                result.setToken(token.toString());
            }
            else {
                result.setStatus(false);
            }

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
