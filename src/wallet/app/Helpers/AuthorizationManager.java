package wallet.app.Helpers;

import com.google.gson.Gson;
import wallet.app.Exceptions.UnauthorizationRequestException;
import wallet.server.Forms.LoginForm;
import wallet.server.Responses.DataResponses.LoginResponse;

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
            FileReader reader = new FileReader("src/wallet/app/UserData/pass.json");
            Gson gson = new Gson();
            Pass pass = gson.fromJson(reader, Pass.class);
            email = pass.getEmail();
            password = pass.getPassword();

            LoginForm loginForm = new LoginForm();
            loginForm.setEmail(email);
            loginForm.setPassword(password);

            Postman<LoginResponse> postman = new Postman<LoginResponse>(false);
            LoginResponse loginResponse = postman.send(loginForm, LoginResponse.class, Postman.Api.LOGIN);

            if(loginResponse.getStatus()){
                token = loginResponse.getToken();
                isAuthorized = true;
            } else {
                isAuthorized = false;
            }

        } catch (Exception e){
            System.out.println("Pass file not exist.");
            //e.printStackTrace();
        }
    }

    public static void authorize(String emailInput, String passwordInput){
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(emailInput);
        loginForm.setPassword(passwordInput);

        Postman<LoginResponse> postman = new Postman<LoginResponse>(false);
        LoginResponse loginResponse = null;
        try {
            loginResponse = postman.send(loginForm, LoginResponse.class, Postman.Api.LOGIN);
        } catch (UnauthorizationRequestException e) {
            e.printStackTrace();
        }

        if(loginResponse.getStatus()){
            email = emailInput;
            password = passwordInput;
            token = loginResponse.getToken();
            isAuthorized = true;

            try {
                FileWriter writer = new FileWriter("src/wallet/app/UserData/pass.json");
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
                //e.printStackTrace();
            }
        } else {
            isAuthorized = false;
        }
    }

    public static void logOut(){
        try {
            FileWriter writer = new FileWriter("src/wallet/app/UserData/pass.json");
            writer.write("");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        isAuthorized = false;
        email = null;
        password = null;
    }

}

class Pass {
    private String email = null;
    private String password = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
