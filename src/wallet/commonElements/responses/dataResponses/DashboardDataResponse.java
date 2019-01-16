package wallet.commonElements.responses.dataResponses;

import javafx.util.Pair;
import wallet.commonElements.responses.BaseResponse;

import java.util.ArrayList;
import java.util.Date;

public class DashboardDataResponse extends BaseResponse {

    private boolean status;
    private Float accountState;
    private Float incomingState;
    private Float outgoingState;
    private ArrayList<Pair<Date, Float>> accountStateDuringMonth = new ArrayList<>();
    private boolean standardMode = true;

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

    public ArrayList<Pair<Date, Float>> getAccountStateDuringMonth() {
        return accountStateDuringMonth;
    }

    public void setAccountStateDuringMonth(ArrayList<Pair<Date, Float>>accountStateDuringMonth) {
        this.accountStateDuringMonth = accountStateDuringMonth;
    }

    public boolean isStandardMode() {
        return standardMode;
    }

    public void setStandardMode(boolean standardMode) {
        this.standardMode = standardMode;
    }
}
