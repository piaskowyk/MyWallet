package wallet.commonElements.responses.serverResponses;

import wallet.commonElements.responses.BaseResponse;

public class NotFound extends BaseResponse {

    public NotFound(){
        super(404, "404 not found");
    }

}
