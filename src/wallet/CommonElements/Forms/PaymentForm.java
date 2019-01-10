package wallet.CommonElements.Forms;

public class PaymentForm {

    private Integer id;
    private Float amount = null;
    private String title = null;
    private String date = null;
    private Type type;
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

    public PaymentsCategory getCategory() {
        return category;
    }

    public void setCategory(PaymentsCategory category) {
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = PaymentsCategory.valueOf(category);
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
