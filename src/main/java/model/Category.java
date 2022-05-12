package model;

import java.util.Optional;

public class Category {

    private long id;
    private String name;
    private Category parent;

    private Category(long id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Optional<Category> getParent() {
        return Optional.ofNullable(parent);
    }

    public static Builder builder(){
        return new Builder();
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
