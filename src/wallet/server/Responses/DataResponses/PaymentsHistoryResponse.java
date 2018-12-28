package wallet.server.Responses.DataResponses;

import wallet.server.Forms.PaymentForm;
import wallet.server.Responses.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class PaymentsHistoryResponse extends BaseResponse {

    private boolean status;
    private List<PaymentForm> paymentsHistory = new ArrayList<PaymentForm>();

    public PaymentsHistoryResponse(){
        super(200, "succes");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PaymentForm> getPaymentsHistory() {
        return paymentsHistory;
    }

    public void setPaymentsHistory(List<PaymentForm> paymentsHistory) {
        this.paymentsHistory = paymentsHistory;
    }
}
