package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class ClientRequest extends Request {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private Long identityId;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientRequest that = (ClientRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(patronymic, that.patronymic) && Objects.equals(passport, that.passport) && Objects.equals(identityId, that.identityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, passport, identityId);
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", passport='" + passport + '\'' +
               ", identityId=" + identityId +
               '}';
    }
}
