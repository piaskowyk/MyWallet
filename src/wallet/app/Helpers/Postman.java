package wallet.app.Helpers;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Postman <T>{

    private final String serverUrl = "http://127.0.0.1:8080";
    private Gson gson = new Gson();
    private HttpClient httpClient = null;

    public Postman(){
        httpClient = HttpClientBuilder.create().build();
    }

    public T send(Object message, Class type, Api request){
        String responseStr = null;
        StringEntity peyloadData = null;
        HttpPost post = new HttpPost(serverUrl + request.url);
        try {
            peyloadData = new StringEntity(gson.toJson(message));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(gson.toJson(message));

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
            e.printStackTrace();
        }
        System.out.println(serverUrl + request.url);
        System.out.println(responseStr);
        Object responseObj = gson.fromJson(responseStr, type);

        return (T)responseObj;
    }

    public enum Api{
        LOGIN("/api/user/login"),
        REGISTER("/api/user/register"),
        ADD_PAYMENTS("/api/wallet/add_payment");

        String url;

        Api(String url){
            this.url = url;
        }
    }

}
