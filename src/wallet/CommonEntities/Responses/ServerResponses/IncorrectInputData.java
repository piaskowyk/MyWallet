package wallet.CommonEntities.Responses.ServerResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class IncorrectInputData extends BaseResponse {

    public IncorrectInputData(){
        super(201, "Incorrect input data.");
    }

}
