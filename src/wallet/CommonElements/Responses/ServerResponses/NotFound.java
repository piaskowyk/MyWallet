package wallet.CommonElements.Responses.ServerResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class NotFound extends BaseResponse {

    public NotFound(){
        super(404, "404 not found");
    }

}
