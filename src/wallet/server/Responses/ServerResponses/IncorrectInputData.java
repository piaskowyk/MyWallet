package wallet.server.Responses.ServerResponses;

import wallet.server.Responses.BaseResponse;

public class IncorrectInputData extends BaseResponse {

    public IncorrectInputData(){
        super(201, "Incorrect input data.");
    }

}
