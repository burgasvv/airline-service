package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class FlightSeat extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long flightId;
    private Long number;
    private Long cabinTypeId;
    private Boolean purchased;
    private Boolean closed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getCabinTypeId() {
        return cabinTypeId;
    }

    public void setCabinTypeId(Long cabinTypeId) {
        this.cabinTypeId = cabinTypeId;
    }

    public Boolean getPurchased() {
        return purchased;
    }

    public void setPurchased(Boolean purchased) {
        this.purchased = purchased;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightSeat that = (FlightSeat) o;
        return Objects.equals(id, that.id) && Objects.equals(flightId, that.flightId) && Objects.equals(number, that.number) &&
               Objects.equals(cabinTypeId, that.cabinTypeId) && Objects.equals(purchased, that.purchased) && Objects.equals(closed, that.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightId, number, cabinTypeId, purchased, closed);
    }

    @Override
    public String toString() {
        return "FlightSeat{" +
               "id=" + id +
               ", flightId=" + flightId +
               ", number=" + number +
               ", cabinTypeId=" + cabinTypeId +
               ", purchased=" + purchased +
               ", closed=" + closed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FlightSeat flightSeat;

        public Builder() {
            flightSeat = new FlightSeat();
        }

        public Builder id(Long id) {
            this.flightSeat.id = id;
            return this;
        }

        public Builder flightId(Long flightId) {
            this.flightSeat.flightId = flightId;
            return this;
        }

        public Builder number(Long number) {
            this.flightSeat.number = number;
            return this;
        }

        public Builder cabinTypeId(Long cabinTypeId) {
            this.flightSeat.cabinTypeId = cabinTypeId;
            return this;
        }

        public Builder purchased(Boolean purchased) {
            this.flightSeat.purchased = purchased;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.flightSeat.closed = closed;
            return this;
        }

        public FlightSeat build() {
            return this.flightSeat;
        }
    }
}
