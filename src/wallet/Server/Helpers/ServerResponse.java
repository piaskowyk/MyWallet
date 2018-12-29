package wallet.Server.Helpers;

import com.google.gson.Gson;
import wallet.Server.Exceptions.RequestDoubleExecutedException;
import wallet.CommonEntities.Responses.ServerResponses.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wallet.CommonEntities.Responses.*;

public class ServerResponse {

    private int statusCode;
    private String contentType;
    private Object data;
    private OutputStream outputStream;
    private PrintWriter dataMaker;
    private boolean executed = false;

    public ServerResponse(OutputStream outputStream){
        this(outputStream, null, "json/application", 200);
    }

    ServerResponse(OutputStream outputStream, Object data){
        this(outputStream, data, "json/application", 200);
    }

    ServerResponse(OutputStream outputStream, Object data, String contentType){
        this(outputStream, data, contentType, 200);
    }

    ServerResponse(OutputStream outputStream, Object data, int statusCode){
        this(outputStream, data, "json/application", statusCode);
    }

    ServerResponse(OutputStream outputStream, Object data, String contentType, int statusCode){
        this.outputStream = outputStream;
        this.dataMaker = new PrintWriter(outputStream);
        this.data = data;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public PrintWriter getDataMaker() {
        return dataMaker;
    }

    public void setDataMaker(PrintWriter dataMaker) {
        this.dataMaker = dataMaker;
    }

    public void response(){
        boolean flag = true;
        String json = null;
        Gson gson = new Gson();

        if(executed) throw new RequestDoubleExecutedException();
        executed = true;

        try{
            json = new String(gson.toJson(data).getBytes(), StandardCharsets.UTF_8);
        }
        catch (Exception e){
            flag = false;
        }

        if(flag){
            dataMaker.println("HTTP/1.1 " + statusCode + " OK");
            dataMaker.println("Content-type: " + contentType);
        } else {
            dataMaker.println("HTTP/1.1 500 OK");
            dataMaker.println("Content-type: " + contentType);
            json = gson.toJson(new ServerError());
        }

        dataMaker.println("Server: piaskowyk");
        dataMaker.println("Date: " + new Date());

        //polish characters have 2 position per char
        Pattern pattern = Pattern.compile("[ęóąśłżźćńĘÓĄŚŁŻŹĆŃ]");
        Matcher matcher = pattern.matcher(json);
        int count = 0;
        while (matcher.find()) count++;

        dataMaker.println("Content-length: " + (json.length() + count));
        dataMaker.println();
        dataMaker.flush();
        dataMaker.println(json);
        dataMaker.flush();

        if (dataMaker != null) dataMaker.close();

        System.out.println(json);
    }

    public void badRequest() {
        makeResponse(new BadRequest());
    }

    public void notFound() {
        makeResponse(new NotFound());
    }

    public void serverError() {
        makeResponse(new ServerError());
    }

    public void incorrectInputData() {
        makeResponse(new IncorrectInputData());
    }

    public void notSupportedMethod() {
        makeResponse(new NotSupportedMethod());
    }

    private void makeResponse(BaseResponse response){
        boolean flag = true;
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(response);

        if(executed) throw new RequestDoubleExecutedException();
        executed = true;

        dataMaker.println("HTTP/1.1 " + response.getCode() + " OK");
        dataMaker.println("Content-type: " + contentType);
        dataMaker.println("Server: piaskowyk");
        dataMaker.println("Date: " + new Date());
        dataMaker.println("Content-length: " + json.length());
        dataMaker.println();
        dataMaker.flush();
        dataMaker.println(json);
        dataMaker.flush();

        if (dataMaker != null) dataMaker.close();
    }

}
