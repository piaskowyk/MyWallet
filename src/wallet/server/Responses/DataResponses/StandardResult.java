package wallet.server.Responses.DataResponses;

import wallet.server.Responses.BaseResponse;

public class StandardResult extends BaseResponse {

    private boolean result;

    public StandardResult(){
        super(200, "succes");
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
