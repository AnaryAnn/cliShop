package model;

import java.util.Objects;
import java.util.Optional;

public class Category {

    private final Long id;
    private final String name;
    private final Category parent; //todo: [Review] кстати у всех полей тоже пишем комменты, это оч полезно, к примеру как тут, потому что нужно гадать, что за парент такой.

    private Category(long id, String name, Category parent) { //todo: [Review] id примитив?
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
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
        private long id;
        private String name;
        private Category parent;

        public Builder setId(long id) {
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
