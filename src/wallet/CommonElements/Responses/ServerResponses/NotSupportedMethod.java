package wallet.CommonElements.Responses.ServerResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class NotSupportedMethod extends BaseResponse {

    public NotSupportedMethod(){
        super(301, "Not supported method, ony GET and POST");
    }

}