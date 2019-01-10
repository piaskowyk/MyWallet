package wallet.App.Untils;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Postman <T>{

    private final String serverUrl = "http://127.0.0.1:8080";
    private Gson gson = new Gson();
    private HttpClient httpClient;
    private boolean checkAuthorization = true;
    private ErrorType errorType = ErrorType.NO_ERROR;

    public Postman(){
        httpClient = HttpClientBuilder.create().build();
    }

    public Postman(boolean checkAuthorization){
        this();
        this.checkAuthorization = checkAuthorization;
    }

    public boolean noError(){
        return errorType == ErrorType.NO_ERROR;
    }

    public T send(Object message, Class type, Api request) {
        if (checkAuthorization && !AuthorizationManager.isAuthorized()){
            AuthorizationManager.authorize();

            if (!AuthorizationManager.isAuthorized()){
                errorType = ErrorType.UNAUTHORIZED;
            }
        }

        String responseStr = null;
        StringEntity peyloadData = null;
        HttpPost post = new HttpPost(serverUrl + request.url);
        try {
            peyloadData = new StringEntity(gson.toJson(message), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            errorType = ErrorType.APPLICATION_ERROR;
        }

        System.out.println("Out: " + gson.toJson(message));

        post.setEntity(peyloadData);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Auth-Token", AuthorizationManager.getToken());
        try {
            HttpResponse response = httpClient.execute(post);
            responseStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MyWallet");
            alert.setHeaderText("Error");
            alert.setContentText("No internet connection.");
            alert.showAndWait();
            errorType = ErrorType.NO_INTERNET_CONNECTION;
        }

        Object responseObj = null;
        if(errorType == ErrorType.NO_ERROR){
            System.out.println(serverUrl + request.url);
            System.out.println("In: " + responseStr);
            responseObj = gson.fromJson(responseStr, type);
        }

        return (T)responseObj;
    }

    public T get(Class type, Api request) {
        if (checkAuthorization && !AuthorizationManager.isAuthorized()){
            AuthorizationManager.authorize();
            if (!AuthorizationManager.isAuthorized()){
                errorType = ErrorType.UNAUTHORIZED;
            }
        }

        String responseStr = null;
        HttpPost post = new HttpPost(serverUrl + request.url);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Auth-Token", AuthorizationManager.getToken());
        try {
            HttpResponse response = httpClient.execute(post);
            responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MyWallet");
            alert.setHeaderText("Error");
            alert.setContentText("No internet connection.");
            alert.showAndWait();
            errorType = ErrorType.NO_INTERNET_CONNECTION;
        }

        Object responseObj = null;
        if(errorType == ErrorType.NO_ERROR){
            System.out.println(serverUrl + request.url);
            System.out.println("In: " + responseStr);
            responseObj = gson.fromJson(responseStr, type);
        }

        return (T)responseObj;
    }

    public ErrorType getError(){
        return errorType;
    }

    public String getErrorMessage(){
        return errorType.getErrorMessage();
    }

    public enum Api{
        LOGIN("/api/user/login"),
        REGISTER("/api/user/register"),
        ADD_PAYMENTS("/api/wallet/add_payment"),
        GET_HISTORY("/api/payments/get_history"),
        REMOVE_PAYMENTS_ITEM("/api/payments/remove_payments_item"),
        EDIT_PAYMENTS_ITEM("/api/payments/edit_payments_item"),
        DASHBOARD_DATA("/api/dashboard/dashboard_data");

        String url;

        Api(String url){
            this.url = url;
        }
    }

}
