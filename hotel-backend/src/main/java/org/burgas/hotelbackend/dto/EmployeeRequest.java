package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class EmployeeRequest extends Request {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private Long identityId;
    private Long filialId;
    private Long positionId;
    private Long imageId;

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

    public Long getFilialId() {
        return filialId;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeRequest that = (EmployeeRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(about, that.about) &&
               Objects.equals(passport, that.passport) && Objects.equals(identityId, that.identityId) &&
               Objects.equals(filialId, that.filialId) && Objects.equals(positionId, that.positionId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, about, passport, identityId, filialId, positionId, imageId);
    }

    @Override
    public String toString() {
        return "EmployeeRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", about='" + about + '\'' +
               ", passport='" + passport + '\'' +
               ", identityId=" + identityId +
               ", filialId=" + filialId +
               ", positionId=" + positionId +
               ", imageId=" + imageId +
               '}';
    }
}
