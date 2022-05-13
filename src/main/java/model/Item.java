package model;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Item {

    private final Long id;
    private final String name;
    private final Category category;
    private final Amount amount;

    private Item(Long id, String name, Category category, Amount amount) {
        this.id = requireNonNull(id, "id");
        this.name = requireNonNull(name, "name");
        this.category = requireNonNull(category, "category");
        this.amount = requireNonNull(amount, "amount");
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id) && name.equals(item.name) && category.equals(item.category) && amount.equals(item.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, amount);
    }

    public static class Builder {

        private Long id;
        private String name;
        private Category category;
        private Amount amount;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setAmount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Item build() {
            return new Item(id, name, category, amount);
        }

    }
}
