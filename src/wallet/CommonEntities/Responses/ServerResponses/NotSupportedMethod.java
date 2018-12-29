package wallet.CommonEntities.Responses.ServerResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class NotSupportedMethod extends BaseResponse {

    public NotSupportedMethod(){
        super(301, "Not supported method, ony GET and POST");
    }

}