package wallet.CommonElements.Entity;

public class PaymentItem {

    private Integer id;
    private Float amount = null;
    private String title = null;
    private PaymentItem.Type type;
    private String date = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PaymentItem.Type getType() {
        return type;
    }

    public void setType(PaymentItem.Type type) {
        this.type = type;
    }

    public void setType(String type) {
        switch (type){
            case "INCOMING":{
                this.type = PaymentItem.Type.INCOMING;
            }break;
            case "OUTCOMING":{
                this.type = PaymentItem.Type.OUTCOMING;
            }break;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public enum Type{
        INCOMING,
        OUTCOMING
    }

}
