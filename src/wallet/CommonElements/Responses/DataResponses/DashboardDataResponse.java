package wallet.CommonElements.Responses.DataResponses;

import wallet.CommonElements.Responses.BaseResponse;

import java.util.HashMap;

public class DashboardDataResponse extends BaseResponse {

    private boolean status;
    private Float accountState;
    private Float incomingState;
    private Float outgoingState;
    private HashMap<Integer, Float> accountStateDuringMonth = new HashMap<>();

    public DashboardDataResponse(){
        super(200, "success");
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

    public Float getOutgoingState() {
        return outgoingState;
    }

    public void setOutgoingState(Float outgoingState) {
        this.outgoingState = outgoingState;
    }

    public HashMap<Integer, Float> getAccountStateDuringMonth() {
        return accountStateDuringMonth;
    }

    public void setAccountStateDuringMonth(HashMap<Integer, Float> accountStateDuringMonth) {
        this.accountStateDuringMonth = accountStateDuringMonth;
    }
}
