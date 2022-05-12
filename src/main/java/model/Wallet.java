package model;

import java.util.HashMap;
import java.util.Map;

public class Wallet {

    private final Long userId;
    private final Map<Currency, Double> balance;

    private Wallet(long userId, Map<Currency, Double> balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public Map<Currency, Double> getBalance() {
        return balance;
    }

    public static class Builder {

        private final Map<Currency, Double> balance = new HashMap<>();
        private long userId;

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setBalance(Currency currency, Double balance) {
            this.balance.put(currency, balance);
            return this;
        }

        public Wallet build() {
            return new Wallet(userId, balance);
        }
    }
}
