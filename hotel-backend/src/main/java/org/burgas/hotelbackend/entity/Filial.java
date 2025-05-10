package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Filial {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long hotelId;
    private Long addressId;
    private Long luxRooms;
    private Long economyRooms;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getLuxRooms() {
        return luxRooms;
    }

    public void setLuxRooms(Long luxRooms) {
        this.luxRooms = luxRooms;
    }

    public Long getEconomyRooms() {
        return economyRooms;
    }

    public void setEconomyRooms(Long economyRooms) {
        this.economyRooms = economyRooms;
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
        Filial filial = (Filial) o;
        return Objects.equals(id, filial.id) && Objects.equals(hotelId, filial.hotelId) && Objects.equals(addressId, filial.addressId) &&
               Objects.equals(luxRooms, filial.luxRooms) && Objects.equals(economyRooms, filial.economyRooms) && Objects.equals(imageId, filial.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hotelId, addressId, luxRooms, economyRooms, imageId);
    }

    @Override
    public String toString() {
        return "Filial{" +
               "id=" + id +
               ", hotelId=" + hotelId +
               ", addressId=" + addressId +
               ", luxRooms=" + luxRooms +
               ", economyRooms=" + economyRooms +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Filial filial;

        public Builder() {
            filial = new Filial();
        }

        public Builder id(Long id) {
            this.filial.id = id;
            return this;
        }

        public Builder hotelId(Long hotelId) {
            this.filial.hotelId = hotelId;
            return this;
        }

        public Builder addressId(Long addressId) {
            this.filial.addressId = addressId;
            return this;
        }

        public Builder luxRooms(Long luxRooms) {
            this.filial.luxRooms = luxRooms;
            return this;
        }

        public Builder economyRooms(Long economyRooms) {
            this.filial.economyRooms = economyRooms;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.filial.imageId = imageId;
            return this;
        }

        public Filial build() {
            return this.filial;
        }
    }
}
