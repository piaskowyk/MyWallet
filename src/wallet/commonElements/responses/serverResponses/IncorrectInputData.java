package wallet.commonElements.responses.serverResponses;

import wallet.commonElements.responses.BaseResponse;

public class IncorrectInputData extends BaseResponse {

    public IncorrectInputData(){
        super(201, "Incorrect input data.");
    }

}
