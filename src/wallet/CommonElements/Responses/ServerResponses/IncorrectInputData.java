package wallet.CommonElements.Responses.ServerResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class IncorrectInputData extends BaseResponse {

    public IncorrectInputData(){
        super(201, "Incorrect input data.");
    }

}
