package application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ItemDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category")
    private CategoryDTO category;

    @JsonProperty("amount")
    private AmountDTO amount;

    private ItemDTO(Long id, String name, CategoryDTO category, AmountDTO amount) {
        this.id = requireNonNull(id, "id");
        this.name = requireNonNull(name, "name");
        this.category = requireNonNull(category, "category");
        this.amount = requireNonNull(amount, "amount");
    }

    public ItemDTO() {
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

    public CategoryDTO getCategory() {
        return category;
    }

    public AmountDTO getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO item = (ItemDTO) o;
        return id.equals(item.id) && name.equals(item.name) && category.equals(item.category) && amount.equals(item.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, amount);
    }

    public static class Builder {

        private Long id;
        private String name;
        private CategoryDTO category;
        private AmountDTO amount;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCategory(CategoryDTO category) {
            this.category = category;
            return this;
        }

        public Builder setAmount(AmountDTO amount) {
            this.amount = amount;
            return this;
        }

        public ItemDTO build() {
            return new ItemDTO(id, name, category, amount);
        }

    }
}
