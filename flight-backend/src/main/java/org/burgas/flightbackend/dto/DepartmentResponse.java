package org.burgas.flightbackend.dto;

@SuppressWarnings("unused")
public final class DepartmentResponse {

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
