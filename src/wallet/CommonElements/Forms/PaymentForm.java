package wallet.CommonElements.Forms;

import wallet.CommonElements.Entity.PaymentCategory;

public class PaymentForm {

    private Integer id;
    private Float amount = null;
    private String title = null;
    private String date = null;
    private Type type;
    private PaymentCategory category;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public PaymentCategory getCategory() {
        return category;
    }

    public void setCategory(PaymentCategory category) {
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = PaymentCategory.valueOf(category);
    }

    public enum Type{
        INCOMING,
        OUTCOMING
    }

}
