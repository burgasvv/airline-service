package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FilialDepartmentResponse extends Response {

    private Long id;
    private FilialResponse filial;
    private DepartmentResponse department;

    public Long getId() {
        return id;
    }

    public FilialResponse getFilial() {
        return filial;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialDepartmentResponse that = (FilialDepartmentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(filial, that.filial) && Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filial, department);
    }

    @Override
    public String toString() {
        return "FilialDepartmentResponse{" +
               "id=" + id +
               ", filial=" + filial +
               ", department=" + department +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FilialDepartmentResponse filialDepartmentResponse;

        public Builder() {
            filialDepartmentResponse = new FilialDepartmentResponse();
        }

        public Builder id(Long id) {
            this.filialDepartmentResponse.id = id;
            return this;
        }

        public Builder filial(FilialResponse filial) {
            this.filialDepartmentResponse.filial = filial;
            return this;
        }

        public Builder department(DepartmentResponse department) {
            this.filialDepartmentResponse.department = department;
            return this;
        }

        public FilialDepartmentResponse build() {
            return this.filialDepartmentResponse;
        }
    }
}
