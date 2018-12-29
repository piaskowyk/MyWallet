package wallet.CommonEntities.Responses.ServerResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class BadRequest extends BaseResponse {

    public BadRequest(){
        super(400, "400 Bad Request");
    }

}
