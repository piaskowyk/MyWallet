package wallet.server;

import com.google.gson.Gson;
import wallet.server.Exceptions.RequestDoubleExecutedException;
import wallet.server.Responses.ServerResponses.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import wallet.server.Responses.*;

public class ServerResponse {

    private int statucCode;
    private String contentType;
    private Object data;
    private OutputStream outputStream;
    private PrintWriter dataMaker;
    private boolean executed = false;

    ServerResponse(OutputStream outputStream){
        this(outputStream, null, "json/application", 200);
    }

    ServerResponse(OutputStream outputStream, Object data){
        this(outputStream, data, "json/application", 200);
    }

    ServerResponse(OutputStream outputStream, Object data, String contentType){
        this(outputStream, data, contentType, 200);
    }

    ServerResponse(OutputStream outputStream, Object data, int statucCode){
        this(outputStream, data, "json/application", statucCode);
    }

    ServerResponse(OutputStream outputStream, Object data, String contentType, int statucCode){
        this.outputStream = outputStream;
        this.dataMaker = new PrintWriter(outputStream);
        this.data = data;
        this.contentType = contentType;
        this.statucCode = statucCode;
    }

    public int getStatucCode() {
        return statucCode;
    }

    public void setStatucCode(int statucCode) {
        this.statucCode = statucCode;
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
        String json = "";
        Gson gson = new Gson();

        if(executed) throw new RequestDoubleExecutedException();
        executed = true;

        try{
            json = gson.toJson(data);
        }
        catch (Exception e){
            flag = false;
        }

        if(flag){
            dataMaker.println("HTTP/1.1 " + statucCode + " OK");
            dataMaker.println("Content-type: " + contentType);
        } else {
            dataMaker.println("HTTP/1.1 500 OK");
            dataMaker.println("Content-type: " + contentType);
            json = gson.toJson(new ServerError());
        }

        dataMaker.println("Server: piaskowyk");
        dataMaker.println("Date: " + new Date());
        dataMaker.println("Content-length: " + json.length());
        dataMaker.println();
        dataMaker.flush();
        dataMaker.println(json);
        dataMaker.flush();

        if (dataMaker != null) dataMaker.close();
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
