package wallet.app.untils;

/*
 * use library:
 * gson: https://github.com/google/gson
 * */

import com.google.gson.Gson;

import wallet.app.views.ViewsManager;
import wallet.commonElements.entity.Pass;
import wallet.commonElements.forms.LoginForm;
import wallet.commonElements.responses.dataResponses.LoginResponse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuthorizationManager {

    private static String token;
    private static boolean isAuthorized = false;
    private static String email = null;
    private static String password = null;

    public static String getToken() {
        return token;
    }

    public static boolean isAuthorized(){
        return isAuthorized;
    }

    public static void authorize(){
        try {
            FileReader reader = new FileReader("src/wallet/app/userData/pass.json");
            Gson gson = new Gson();
            Pass pass = gson.fromJson(reader, Pass.class);
            email = pass.getEmail();
            password = pass.getPassword();

            LoginForm loginForm = new LoginForm();
            loginForm.setEmail(email);
            loginForm.setPassword(password);

            Postman<LoginResponse> postman = new Postman<>(false);
            LoginResponse loginResponse = postman.send(loginForm, LoginResponse.class, Postman.Api.LOGIN);

            if(loginResponse.getStatus()){
                token = loginResponse.getToken();
                isAuthorized = true;
            } else {
                isAuthorized = false;
            }

        } catch (Exception e){
            System.out.println("Pass file not exist.");
        }
    }

    public static void authorize(String emailInput, String passwordInput){
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(emailInput);
        loginForm.setPassword(passwordInput);

        Postman<LoginResponse> postman = new Postman<>(false);
        LoginResponse loginResponse = postman.send(loginForm, LoginResponse.class, Postman.Api.LOGIN);

        if(postman.noError() && loginResponse.getStatus()){
            email = emailInput;
            password = passwordInput;
            token = loginResponse.getToken();
            isAuthorized = true;

            try {
                FileWriter writer = new FileWriter("src/wallet/app/userData/pass.json");
                Pass pass = new Pass();
                pass.setEmail(email);
                pass.setPassword(password);
                Gson gson = new Gson();
                System.out.println(pass.getEmail());
                System.out.println(pass.getEmail());
                String passString = gson.toJson(pass);
                writer.write(passString);
                writer.close();

            } catch (IOException e) {
                System.out.println("Pass file not exist.");
            }
        } else {
            isAuthorized = false;
        }
    }

    public static void logOut(){
        try {
            FileWriter writer = new FileWriter("src/wallet/app/userData/pass.json");
            writer.write("");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        email = null;
        password = null;
        token = null;
        isAuthorized = false;
        ViewsManager.clearViewData();
    }
}

