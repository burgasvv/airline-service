package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class EmployeeResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private IdentityResponse identity;
    private AddressResponse address;
    private PositionResponse position;
    private FilialDepartmentResponse filialDepartment;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getAbout() {
        return about;
    }

    public String getPassport() {
        return passport;
    }

    public IdentityResponse getIdentity() {
        return identity;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public PositionResponse getPosition() {
        return position;
    }

    public FilialDepartmentResponse getFilialDepartment() {
        return filialDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeResponse that = (EmployeeResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(about, that.about) && Objects.equals(passport, that.passport) &&
               Objects.equals(identity, that.identity) && Objects.equals(address, that.address) && Objects.equals(position, that.position) &&
               Objects.equals(filialDepartment, that.filialDepartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, about, passport, identity, address, position, filialDepartment);
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", about='" + about + '\'' +
               ", passport='" + passport + '\'' +
               ", identity=" + identity +
               ", address=" + address +
               ", position=" + position +
               ", filialDepartment=" + filialDepartment +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final EmployeeResponse employeeResponse;

        public Builder() {
            employeeResponse = new EmployeeResponse();
        }

        public Builder id(Long id) {
            this.employeeResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.employeeResponse.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.employeeResponse.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.employeeResponse.patronymic = patronymic;
            return this;
        }

        public Builder about(String about) {
            this.employeeResponse.about = about;
            return this;
        }

        public Builder passport(String passport) {
            this.employeeResponse.passport = passport;
            return this;
        }

        public Builder identity(IdentityResponse identity) {
            this.employeeResponse.identity = identity;
            return this;
        }

        public Builder address(AddressResponse address) {
            this.employeeResponse.address = address;
            return this;
        }

        public Builder position(PositionResponse position) {
            this.employeeResponse.position = position;
            return this;
        }

        public Builder filialDepartment(FilialDepartmentResponse filialDepartment) {
            this.employeeResponse.filialDepartment = filialDepartment;
            return this;
        }

        public EmployeeResponse build() {
            return this.employeeResponse;
        }
    }
}
