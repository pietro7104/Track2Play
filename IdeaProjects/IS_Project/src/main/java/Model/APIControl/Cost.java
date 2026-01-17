package Model.APIControl;

public class Cost {
    public float amount;
    public String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Cost(float amount, String currency){
        this.amount = amount;
        this.currency = currency;
    }
}
