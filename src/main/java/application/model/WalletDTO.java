package application.model;

public class WalletDTO {

    private final Long userId;
    private final Double balance;
    private final Currency currency;

    private WalletDTO(Long userId, Double balance, Currency currency) {
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public static class Builder {

        private Long userId;
        private Double balance;
        private Currency currency;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setBalance(Double balance) {
            this.balance = balance;
            return this;
        }

        public Builder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public WalletDTO build() {
            return new WalletDTO(userId, balance, currency);
        }
    }
}
