package wallet.commonElements.responses.dataResponses;

import wallet.commonElements.responses.BaseResponse;

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
