package model;

import java.util.Objects;
import java.util.Optional;

public class Category {

    /**
     * ID категории
     */
    private final Long id;
    /**
     * Название категории
     */
    private final String name;
    /**
     * Родительская категория
     */
    private final Category parent;

    private Category(Long id, String name, Category parent) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.parent = parent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id.equals(category.id) && name.equals(category.name) && Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parent);
    }

    public Optional<Category> getParent() {
        return Optional.ofNullable(parent);
    }

    public static class Builder {
        private Long id;
        private String name;
        private Category parent;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setParent(Category parent) {
            this.parent = parent;
            return this;
        }

        public Category build() {
            return new Category(id, name, parent);
        }
    }
}
