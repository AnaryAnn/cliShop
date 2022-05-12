package model;

import static java.util.Objects.requireNonNull;

public class Amount {

    private Currency currency;
    private double sum;

    public Amount(Currency currency, double price) {
        this.currency = requireNonNull(currency, "currency");
        this.sum = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
