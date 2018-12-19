package wallet.server.Responses;

public class ServerError extends Response {

    public ServerError(){
        super(500, "500 Server Error");
    }

}
