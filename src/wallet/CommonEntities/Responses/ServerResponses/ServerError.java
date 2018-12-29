package wallet.CommonEntities.Responses.ServerResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class ServerError extends BaseResponse {

    public ServerError(){
        super(500, "500 Server Error");
    }

}
