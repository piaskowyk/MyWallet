package wallet.CommonElements.Entity;

public enum PaymentCategory {
    FOOD("food"),
    ELECTRONIC("electronic"),
    OTHER("other"),
    IN("in");

    private String name;

    PaymentCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
