package wallet.CommonElements.Responses.ServerResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class ServerError extends BaseResponse {

    public ServerError(){
        super(500, "500 Server Error");
    }

}
