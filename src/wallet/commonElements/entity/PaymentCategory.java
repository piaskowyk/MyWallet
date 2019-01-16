package wallet.commonElements.entity;

public enum PaymentCategory {
    FOOD("FOOD"),
    ELECTRONIC("ELECTRONIC"),
    OTHER("OTHER"),
    IN("IN");

    private String name;

    PaymentCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
