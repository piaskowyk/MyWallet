package wallet.server.Responses.ServerResponses;

import wallet.server.Responses.BaseResponse;

public class ServerError extends BaseResponse {

    public ServerError(){
        super(500, "500 Server Error");
    }

}
