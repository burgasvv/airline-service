package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Room extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    public Long getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Long hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(number, room.number) && Objects.equals(roomTypeId, room.roomTypeId) &&
               Objects.equals(filialId, room.filialId) && Objects.equals(description, room.description) &&
               Objects.equals(rented, room.rented) && Objects.equals(rentedFrom, room.rentedFrom) &&
               Objects.equals(rentedTo, room.rentedTo) && Objects.equals(hourPrice, room.hourPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, roomTypeId, filialId, description, rented, rentedFrom, rentedTo, hourPrice);
    }

    @Override
    public String toString() {
        return "Room{" +
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Room room;

        public Builder() {
            room = new Room();
        }

        public Builder id(Long id) {
            this.room.id = id;
            return this;
        }

        public Builder number(Long number) {
            this.room.number = number;
            return this;
        }

        public Builder roomTypeId(Long roomTypeId) {
            this.room.roomTypeId = roomTypeId;
            return this;
        }

        public Builder filialId(Long filialId) {
            this.room.filialId = filialId;
            return this;
        }

        public Builder description(String description) {
            this.room.description = description;
            return this;
        }

        public Builder rented(Boolean rented) {
            this.room.rented = rented;
            return this;
        }

        public Builder rentedFrom(LocalDateTime rentedFrom) {
            this.room.rentedFrom = rentedFrom;
            return this;
        }

        public Builder rentedTo(LocalDateTime rentedTo) {
            this.room.rentedTo = rentedTo;
            return this;
        }

        public Builder hourPrice(Long hourPrice) {
            this.room.hourPrice = hourPrice;
            return this;
        }

        public Room build() {
            return this.room;
        }
    }
}
