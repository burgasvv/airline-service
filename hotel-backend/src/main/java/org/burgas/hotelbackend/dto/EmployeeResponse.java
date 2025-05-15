package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class EmployeeResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private IdentityResponse identity;
    private FilialResponse filial;
    private PositionResponse position;
    private Long imageId;

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

    public FilialResponse getFilial() {
        return filial;
    }

    public PositionResponse getPosition() {
        return position;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeResponse that = (EmployeeResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(about, that.about) &&
               Objects.equals(passport, that.passport) && Objects.equals(identity, that.identity) &&
               Objects.equals(filial, that.filial) && Objects.equals(position, that.position) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, about, passport, identity, filial, position, imageId);
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

        public Builder filial(FilialResponse filial) {
            this.employeeResponse.filial = filial;
            return this;
        }

        public Builder position(PositionResponse position) {
            this.employeeResponse.position = position;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.employeeResponse.imageId = imageId;
            return this;
        }

        public EmployeeResponse build() {
            return this.employeeResponse;
        }
    }
}
