package wallet.server.Responses.DataResponses;

import wallet.server.Responses.BaseResponse;

public class StandardResult extends BaseResponse {

    private boolean status;

    public StandardResult(){
        super(200, "succes");
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
