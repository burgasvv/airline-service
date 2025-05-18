package org.burgas.hotelbackend.dto;

import org.burgas.hotelbackend.entity.RoomType;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class RoomResponse extends Response {

    private Long id;
    private Long number;
    private RoomType roomType;
    private FilialResponse filial;
    private String description;
    private Boolean rented;
    private String rentedFrom;
    private String rentedTo;
    private Long hourPrice;

    public Long getId() {
        return id;
    }

    public Long getNumber() {
        return number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public FilialResponse getFilial() {
        return filial;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getRented() {
        return rented;
    }

    public String getRentedFrom() {
        return rentedFrom;
    }

    public String getRentedTo() {
        return rentedTo;
    }

    public Long getHourPrice() {
        return hourPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomResponse that = (RoomResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(roomType, that.roomType) &&
               Objects.equals(filial, that.filial) && Objects.equals(description, that.description) && Objects.equals(rented, that.rented) &&
               Objects.equals(rentedFrom, that.rentedFrom) && Objects.equals(rentedTo, that.rentedTo) && Objects.equals(hourPrice, that.hourPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, roomType, filial, description, rented, rentedFrom, rentedTo, hourPrice);
    }

    @Override
    public String toString() {
        return "RoomResponse{" +
               "id=" + id +
               ", number=" + number +
               ", roomType=" + roomType +
               ", filial=" + filial +
               ", description='" + description + '\'' +
               ", rented=" + rented +
               ", rentedFrom='" + rentedFrom + '\'' +
               ", rentedTo='" + rentedTo + '\'' +
               ", hourPrice=" + hourPrice +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RoomResponse roomResponse;

        public Builder() {
            roomResponse = new RoomResponse();
        }

        public Builder id(Long id) {
            this.roomResponse.id = id;
            return this;
        }

        public Builder number(Long number) {
            this.roomResponse.number = number;
            return this;
        }

        public Builder roomType(RoomType roomType) {
            this.roomResponse.roomType = roomType;
            return this;
        }

        public Builder filial(FilialResponse filial) {
            this.roomResponse.filial = filial;
            return this;
        }

        public Builder description(String description) {
            this.roomResponse.description = description;
            return this;
        }

        public Builder rented(Boolean rented) {
            this.roomResponse.rented = rented;
            return this;
        }

        public Builder rentedFrom(String rentedFrom) {
            this.roomResponse.rentedFrom = rentedFrom;
            return this;
        }

        public Builder rentedTo(String rentedTo) {
            this.roomResponse.rentedTo = rentedTo;
            return this;
        }

        public Builder hourPrice(Long hourPrice) {
            this.roomResponse.hourPrice = hourPrice;
            return this;
        }

        public RoomResponse build() {
            return this.roomResponse;
        }
    }
}
