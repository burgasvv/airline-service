package org.burgas.hotelbackend.dto;

import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class RentRoom {

    private Long clientId;
    private Long roomId;
    private LocalDateTime rentedFrom;
    private LocalDateTime rentedTo;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RentRoom rentRoom = (RentRoom) o;
        return Objects.equals(clientId, rentRoom.clientId) && Objects.equals(roomId, rentRoom.roomId) &&
               Objects.equals(rentedFrom, rentRoom.rentedFrom) && Objects.equals(rentedTo, rentRoom.rentedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, roomId, rentedFrom, rentedTo);
    }

    @Override
    public String toString() {
        return "RentRoom{" +
               "clientId=" + clientId +
               ", roomId=" + roomId +
               ", rentedFrom=" + rentedFrom +
               ", rentedTo=" + rentedTo +
               '}';
    }
}
