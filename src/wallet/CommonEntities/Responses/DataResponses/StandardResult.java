package wallet.CommonEntities.Responses.DataResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class StandardResult extends BaseResponse {

    private boolean status;

    public StandardResult(){
        super(200, "succes");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
