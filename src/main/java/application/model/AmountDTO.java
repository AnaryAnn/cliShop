package application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.requireNonNull;
public class AmountDTO {

    @JsonProperty("currency")
    private final Currency currency;

    @JsonProperty("sum")
    private final Double sum;

    @JsonCreator
    public AmountDTO(@JsonProperty("currency") Currency currency, @JsonProperty("sum") Double price) {
        this.currency = requireNonNull(currency, "currency");
        this.sum = price;
    }

    public AmountDTO() {
        this.currency = Currency.RUB;
        this.sum = 0.0;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getSum() {
        return sum;
    }
}
