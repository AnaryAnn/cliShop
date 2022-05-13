package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Double.compare(amount.sum, sum) == 0 && currency == amount.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, sum);
    }
}
