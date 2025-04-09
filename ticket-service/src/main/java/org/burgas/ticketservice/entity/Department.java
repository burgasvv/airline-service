package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Department implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private String description;

    @Transient
    private Boolean isNew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
               Objects.equals(description, that.description) && Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isNew);
    }

    @Override
    public String toString() {
        return "Department{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Department department;

        public Builder() {
            department = new Department();
        }

        public Builder id(Long id) {
            this.department.id = id;
            return this;
        }

        public Builder name(String name) {
            this.department.name = name;
            return this;
        }

        public Builder description(String description) {
            this.department.description = description;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.department.isNew = isNew;
            return this;
        }

        public Department build() {
            return this.department;
        }
    }
}
