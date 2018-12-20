package wallet.server.Responses.ServerResponses;

import wallet.server.Responses.BaseResponse;

public class NotFound extends BaseResponse {

    public NotFound(){
        super(404, "404 not found");
    }

}
