package wallet.CommonEntities.Responses.DataResponses;

import wallet.CommonEntities.Responses.BaseResponse;

import java.util.HashMap;

public class DashboardDataResponse extends BaseResponse {

    private boolean status;
    private Float accountState;
    private Float incomingState;
    private Float outcomingState;
    private HashMap<Integer, Float> accountStateDuringMonth = new HashMap<Integer, Float>();

    public DashboardDataResponse(){
        super(200, "succes");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Float getAccountState() {
        return accountState;
    }

    public void setAccountState(Float accountState) {
        this.accountState = accountState;
    }

    public Float getIncomingState() {
        return incomingState;
    }

    public void setIncomingState(Float incomingState) {
        this.incomingState = incomingState;
    }

    public Float getOutcomingState() {
        return outcomingState;
    }

    public void setOutcomingState(Float outcomingState) {
        this.outcomingState = outcomingState;
    }

    public HashMap<Integer, Float> getAccountStateDuringMonth() {
        return accountStateDuringMonth;
    }

    public void setAccountStateDuringMonth(HashMap<Integer, Float> accountStateDuringMonth) {
        this.accountStateDuringMonth = accountStateDuringMonth;
    }
}
