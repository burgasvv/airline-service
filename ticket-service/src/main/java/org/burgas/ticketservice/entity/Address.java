package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Address implements Persistable<Long> {

    @Id
    private Long id;
    private Long cityId;
    private String street;
    private String house;
    private String apartment;

    @Transient
    private Boolean isNew;

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
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(cityId, address.cityId) &&
               Objects.equals(street, address.street) && Objects.equals(house, address.house) &&
               Objects.equals(apartment, address.apartment) && Objects.equals(isNew, address.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityId, street, house, apartment, isNew);
    }

    @Override
    public String toString() {
        return "Address{" +
               "id=" + id +
               ", cityId=" + cityId +
               ", street='" + street + '\'' +
               ", house='" + house + '\'' +
               ", apartment='" + apartment + '\'' +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Address address;

        public Builder() {
            address = new Address();
        }

        public Builder id(Long id) {
            this.address.id = id;
            return this;
        }

        public Builder cityId(Long cityId) {
            this.address.cityId = cityId;
            return this;
        }

        public Builder street(String street) {
            this.address.street = street;
            return this;
        }

        public Builder house(String house) {
            this.address.house = house;
            return this;
        }

        public Builder apartment(String apartment) {
            this.address.apartment = apartment;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.address.isNew = isNew;
            return this;
        }

        public Address build() {
            return this.address;
        }
    }
}
