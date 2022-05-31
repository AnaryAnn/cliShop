package application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CategoryDTO {

    /**
     * ID категории
     */
    @JsonProperty("id")
    private final Long id;

    /**
     * Название категории
     */
    @JsonProperty("name")
    private final String name;

    @JsonCreator
    private CategoryDTO(@JsonProperty("id") Long id, @JsonProperty("name") String name) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
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
        CategoryDTO category = (CategoryDTO) obj;
        return id.equals(category.id) && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public CategoryDTO build() {
            return new CategoryDTO(id, name);
        }
    }
}
