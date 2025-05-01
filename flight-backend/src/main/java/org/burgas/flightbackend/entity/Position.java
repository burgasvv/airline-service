package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Position {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long departmentId;

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id) && Objects.equals(name, position.name) && Objects.equals(description, position.description) &&
               Objects.equals(departmentId, position.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, departmentId);
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

        public Position build() {
            return this.position;
        }
    }
}
