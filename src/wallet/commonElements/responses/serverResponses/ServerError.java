package wallet.commonElements.responses.serverResponses;

import wallet.commonElements.responses.BaseResponse;

public class ServerError extends BaseResponse {

    public ServerError(){
        super(500, "500 server Error");
    }

}
