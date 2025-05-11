package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FilialDepartmentRequest extends Request {

    private Long id;
    private FilialRequest filial;
    private DepartmentRequest department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FilialRequest getFilial() {
        return filial;
    }

    public void setFilial(FilialRequest filial) {
        this.filial = filial;
    }

    public DepartmentRequest getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentRequest department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialDepartmentRequest that = (FilialDepartmentRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(filial, that.filial) && Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filial, department);
    }

    @Override
    public String toString() {
        return "FilialDepartmentRequest{" +
               "id=" + id +
               ", filial=" + filial +
               ", department=" + department +
               '}';
    }
}
