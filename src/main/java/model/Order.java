package model;

import java.util.Collection;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Long userId;
    private final Status status;
    private final Collection<Item> items;
    private final Amount totalAmount;

    private Order(Long id, Long userId, Status status, Collection<Item> items, Amount totalAmount) {
        this.id = Objects.requireNonNull(id, "id");
        this.status = Objects.requireNonNull(status, "status");
        this.items = Objects.requireNonNull(items, "items");
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount");
        this.userId = Objects.requireNonNull(userId, "userId");
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) && userId.equals(order.userId) && status == order.status
                && items.equals(order.items) && totalAmount.equals(order.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, status, items, totalAmount);
    }

    public static class Builder {

        private Long id;
        private Long userId;
        private Status status;
        private Collection<Item> items;
        private Amount totalAmount;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setItems(Collection<Item> items) {
            this.items = items;
            return this;
        }

        public Builder setTotalAmount(Amount totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Order build() {
            return new Order(id, userId, status, items, totalAmount);
        }
    }
}
