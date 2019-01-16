package wallet.commonElements.responses.serverResponses;

import wallet.commonElements.responses.BaseResponse;

public class NotSupportedMethod extends BaseResponse {

    public NotSupportedMethod(){
        super(301, "Not supported method, ony GET and POST");
    }

}