package model;

public class Item {

    private final Long id;
    private final String name;
    private final Category category;
    private final Amount amount;

    private Item(long id, String name, Category category, Amount amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
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

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

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

        public Item build(){
            return new Item(id, name, category, amount);
        }

    }
}
