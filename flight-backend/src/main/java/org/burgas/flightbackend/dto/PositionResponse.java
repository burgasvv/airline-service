package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class PositionResponse extends Response {

    private Long id;
    private String name;
    private String description;
    private DepartmentResponse department;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PositionResponse that = (PositionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
               Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, department);
    }

    @Override
    public String toString() {
        return "PositionResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", department=" + department +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final PositionResponse positionResponse;

        public Builder() {
            positionResponse = new PositionResponse();
        }

        public Builder id(Long id) {
            this.positionResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.positionResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            this.positionResponse.description = description;
            return this;
        }

        public Builder department(DepartmentResponse department) {
            this.positionResponse.department = department;
            return this;
        }

        public PositionResponse build() {
            return this.positionResponse;
        }
    }
}
