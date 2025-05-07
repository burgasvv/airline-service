package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class AddressRequest {

    private Long id;
    private Long cityId;
    private String street;
    private String house;
    private String apartment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AddressRequest that = (AddressRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(cityId, that.cityId) && Objects.equals(street, that.street) &&
               Objects.equals(house, that.house) && Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityId, street, house, apartment);
    }

    @Override
    public String toString() {
        return "AddressRequest{" +
               "id=" + id +
               ", cityId=" + cityId +
               ", street='" + street + '\'' +
               ", house='" + house + '\'' +
               ", apartment='" + apartment + '\'' +
               '}';
    }
}
