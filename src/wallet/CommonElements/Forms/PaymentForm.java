package wallet.CommonElements.Forms;

public class PaymentForm {

    private Integer id;
    private Float amount = null;
    private String title = null;
    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type) {
        switch (type){
            case "INCOMING":{
                this.type = Type.INCOMING;
            }break;
            case "OUTCOMING":{
                this.type = Type.OUTCOMING;
            }break;
        }
    }

    public enum Type{
        INCOMING,
        OUTCOMING
    }

}
