package model;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
//todo: [Review] Нам действительно тут сеттеры нужны? Почему объект не immutable?
public class Amount {

    private Currency currency; //todo: [Review] final?
    private double sum; //todo: [Review] final?

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

    //todo: [Review] Нам тут equals и hashcode точно нужны?
    @Override
    public boolean equals(Object o) { //todo: [Review] не говорящее название переменных, да и еще с одной буквы нехорошо
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
