package wallet.server.Responses.DataResponses;

import wallet.server.Entity.PaymentItem;
import wallet.server.Forms.PaymentForm;
import wallet.server.Responses.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class PaymentsHistoryResponse extends BaseResponse {

    private boolean status;
    private List<PaymentItem> paymentsHistory = new ArrayList<>();

    public PaymentsHistoryResponse(){
        super(200, "succes");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PaymentItem> getPaymentsHistory() {
        return paymentsHistory;
    }

    public void setPaymentsHistory(List<PaymentItem> paymentsHistory) {
        this.paymentsHistory = paymentsHistory;
    }
}
