package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Position implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private String description;
    private Long departmentId;

    @Transient
    private Boolean isNew;

    @Override
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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
        Position position = (Position) o;
        return Objects.equals(id, position.id) && Objects.equals(name, position.name) &&
               Objects.equals(description, position.description) && Objects.equals(departmentId, position.departmentId) &&
               Objects.equals(isNew, position.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, departmentId, isNew);
    }

    @Override
    public String toString() {
        return "Position{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", departmentId=" + departmentId +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Position position;

        public Builder() {
            position = new Position();
        }

        public Builder id(Long id) {
            this.position.id = id;
            return this;
        }

        public Builder name(String name) {
            this.position.name = name;
            return this;
        }

        public Builder description(String description) {
            this.position.description = description;
            return this;
        }

        public Builder departmentId(Long departmentId) {
            this.position.departmentId = departmentId;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.position.isNew = isNew;
            return this;
        }

        public Position build() {
            return this.position;
        }
    }
}
