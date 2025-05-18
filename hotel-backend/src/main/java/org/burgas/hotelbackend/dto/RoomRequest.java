package org.burgas.hotelbackend.dto;

import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class RoomRequest extends Request {

    private Long id;
    private Long number;
    private Long roomTypeId;
    private Long filialId;
    private String description;
    private Boolean rented;
    private LocalDateTime rentedFrom;
    private LocalDateTime rentedTo;
    private Long hourPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getFilialId() {
        return filialId;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    public LocalDateTime getRentedFrom() {
        return rentedFrom;
    }

    public void setRentedFrom(LocalDateTime rentedFrom) {
        this.rentedFrom = rentedFrom;
    }

    public LocalDateTime getRentedTo() {
        return rentedTo;
    }

    public void setRentedTo(LocalDateTime rentedTo) {
        this.rentedTo = rentedTo;
    }

    public Long getHoutPrice() {
        return hourPrice;
    }

    public void setHourPrice(Long hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomRequest that = (RoomRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(roomTypeId, that.roomTypeId) &&
               Objects.equals(filialId, that.filialId) && Objects.equals(description, that.description) && Objects.equals(rented, that.rented) &&
               Objects.equals(rentedFrom, that.rentedFrom) && Objects.equals(rentedTo, that.rentedTo) && Objects.equals(hourPrice, that.hourPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, roomTypeId, filialId, description, rented, rentedFrom, rentedTo, hourPrice);
    }

    @Override
    public String toString() {
        return "RoomRequest{" +
               "id=" + id +
               ", number=" + number +
               ", roomTypeId=" + roomTypeId +
               ", filialId=" + filialId +
               ", description='" + description + '\'' +
               ", rented=" + rented +
               ", rentedFrom=" + rentedFrom +
               ", rentedTo=" + rentedTo +
               ", hourPrice=" + hourPrice +
               '}';
    }
}
