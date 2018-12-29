package wallet.CommonElements.Responses.ServerResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class BadRequest extends BaseResponse {

    public BadRequest(){
        super(400, "400 Bad Request");
    }

}
