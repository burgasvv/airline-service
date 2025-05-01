package org.burgas.flightbackend.dto;

@SuppressWarnings("unused")
public final class FilialDepartmentRequest {

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
}
