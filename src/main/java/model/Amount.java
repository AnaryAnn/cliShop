package model;

import static java.util.Objects.requireNonNull;
public class Amount {

    private final Currency currency;
    private final double sum;

    public Amount(Currency currency, double price) {
        this.currency = requireNonNull(currency, "currency");
        this.sum = price;
    }

    public Amount() {
        this.currency = Currency.RUB;
        this.sum = 0;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getSum() {
        return sum;
    }
}
