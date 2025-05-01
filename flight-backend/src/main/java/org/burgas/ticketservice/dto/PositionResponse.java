package org.burgas.ticketservice.dto;

@SuppressWarnings("unused")
public final class PositionResponse {

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
