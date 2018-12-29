package wallet.CommonElements.Responses.DataResponses;

import wallet.CommonElements.Responses.BaseResponse;

public class StandardResult extends BaseResponse {

    private boolean status;

    public StandardResult(){
        super(200, "success");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
