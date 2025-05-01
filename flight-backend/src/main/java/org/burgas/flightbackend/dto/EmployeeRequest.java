package org.burgas.flightbackend.dto;

@SuppressWarnings("unused")
public final class EmployeeRequest {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private Long identityId;
    private AddressRequest address;
    private Long positionId;
    private Long filialDepartmentId;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getFilialDepartmentId() {
        return filialDepartmentId;
    }

    public void setFilialDepartmentId(Long filialDepartmentId) {
        this.filialDepartmentId = filialDepartmentId;
    }
}
