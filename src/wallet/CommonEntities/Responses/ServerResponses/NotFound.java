package wallet.CommonEntities.Responses.ServerResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class NotFound extends BaseResponse {

    public NotFound(){
        super(404, "404 not found");
    }

}
