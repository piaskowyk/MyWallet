package wallet.CommonElements.Entity;

import wallet.CommonElements.Forms.PaymentForm;

public class PaymentItem {

    private Integer id;
    private Float amount = null;
    private String title = null;
    private PaymentItem.Type type;
    private String date = null;
    private PaymentsCategory category;

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
        this.type = Type.valueOf(type);
    }

    public PaymentItem.PaymentsCategory getCategory() {
        return category;
    }

    public void setCategory(PaymentItem.PaymentsCategory category) {
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = PaymentItem.PaymentsCategory.valueOf(category);
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

    public enum  PaymentsCategory {
        FOOD("food"),
        ELECTRONIC("electronic"),
        OTHER("other"),
        IN("in");

        private String name;

        PaymentsCategory(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
