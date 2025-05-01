package org.burgas.ticketservice.dto;

import org.burgas.ticketservice.entity.CabinType;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class FlightSeatResponse {

    private Long id;
    private String flightNumber;
    private Long number;
    private CabinType cabinType;
    private Boolean purchased;
    private Boolean closed;

    public Long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Long getNumber() {
        return number;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public Boolean getPurchased() {
        return purchased;
    }

    public Boolean getClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightSeatResponse that = (FlightSeatResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(flightNumber, that.flightNumber) &&
               Objects.equals(number, that.number) && Objects.equals(cabinType, that.cabinType) &&
               Objects.equals(purchased, that.purchased) && Objects.equals(closed, that.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightNumber, number, cabinType, purchased, closed);
    }

    @Override
    public String toString() {
        return "FlightSeatResponse{" +
               "id=" + id +
               ", flightNumber='" + flightNumber + '\'' +
               ", number=" + number +
               ", cabinType=" + cabinType +
               ", purchased=" + purchased +
               ", closed=" + closed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FlightSeatResponse flightSeatResponse;

        public Builder() {
            flightSeatResponse = new FlightSeatResponse();
        }

        public Builder id(Long id) {
            this.flightSeatResponse.id = id;
            return this;
        }

        public Builder flightNumber(String flightNumber) {
            this.flightSeatResponse.flightNumber = flightNumber;
            return this;
        }

        public Builder number(Long number) {
            this.flightSeatResponse.number = number;
            return this;
        }

        public Builder cabinType(CabinType cabinType) {
            this.flightSeatResponse.cabinType = cabinType;
            return this;
        }

        public Builder purchased(Boolean purchased) {
            this.flightSeatResponse.purchased = purchased;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.flightSeatResponse.closed = closed;
            return this;
        }

        public FlightSeatResponse build() {
            return this.flightSeatResponse;
        }
    }
}
