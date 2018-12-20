package wallet.server.Responses.ServerResponses;

import wallet.server.Responses.BaseResponse;

public class BadRequest extends BaseResponse {

    public BadRequest(){
        super(400, "400 Bad Request");
    }

}
