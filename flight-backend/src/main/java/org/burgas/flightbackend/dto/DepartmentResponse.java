package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class DepartmentResponse extends Response {

    private Long id;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentResponse that = (DepartmentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "DepartmentResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final DepartmentResponse departmentResponse;

        public Builder() {
            departmentResponse = new DepartmentResponse();
        }

        public Builder id(Long id) {
            this.departmentResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.departmentResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            this.departmentResponse.description = description;
            return this;
        }

        public DepartmentResponse build() {
            return this.departmentResponse;
        }
    }
}
