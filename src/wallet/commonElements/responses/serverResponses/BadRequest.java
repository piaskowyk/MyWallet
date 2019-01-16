package wallet.commonElements.responses.serverResponses;

import wallet.commonElements.responses.BaseResponse;

public class BadRequest extends BaseResponse {

    public BadRequest(){
        super(400, "400 Bad Request");
    }

}
