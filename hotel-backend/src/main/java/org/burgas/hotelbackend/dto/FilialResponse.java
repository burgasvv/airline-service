package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class FilialResponse extends Response {

    private Long id;
    private HotelResponse hotel;
    private AddressResponse address;
    private Long luxRooms;
    private Long economyRooms;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public HotelResponse getHotel() {
        return hotel;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public Long getLuxRooms() {
        return luxRooms;
    }

    public Long getEconomyRooms() {
        return economyRooms;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialResponse that = (FilialResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(hotel, that.hotel) && Objects.equals(address, that.address) &&
               Objects.equals(luxRooms, that.luxRooms) && Objects.equals(economyRooms, that.economyRooms) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hotel, address, luxRooms, economyRooms, imageId);
    }

    @Override
    public String toString() {
        return "FilialResponse{" +
               "id=" + id +
               ", hotel=" + hotel +
               ", address=" + address +
               ", luxRooms=" + luxRooms +
               ", economyRooms=" + economyRooms +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FilialResponse filialResponse;

        public Builder() {
            filialResponse = new FilialResponse();
        }

        public Builder id(Long id) {
            this.filialResponse.id = id;
            return this;
        }

        public Builder hotel(HotelResponse hotel) {
            this.filialResponse.hotel = hotel;
            return this;
        }

        public Builder address(AddressResponse address) {
            this.filialResponse.address = address;
            return this;
        }

        public Builder luxRooms(Long luxRooms) {
            this.filialResponse.luxRooms = luxRooms;
            return this;
        }

        public Builder economyRooms(Long economyRooms) {
            this.filialResponse.economyRooms = economyRooms;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.filialResponse.imageId = imageId;
            return this;
        }

        public FilialResponse build() {
            return this.filialResponse;
        }
    }
}
