package wallet.server.Responses.ServerResponses;

import wallet.server.Responses.BaseResponse;

public class NotSupportedMethod extends BaseResponse {

    public NotSupportedMethod(){
        super(301, "Not supported method, ony GET and POST");
    }

}