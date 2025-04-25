package org.burgas.ticketservice.dto;

import org.burgas.ticketservice.entity.CabinType;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class TicketResponse {

    private Long id;
    private FlightResponse flight;
    private CabinType cabinType;
    private Long price;
    private Long amount;

    public Long getId() {
        return id;
    }

    public FlightResponse getFlight() {
        return flight;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public Long getPrice() {
        return price;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketResponse that = (TicketResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(flight, that.flight) && Objects.equals(cabinType, that.cabinType) &&
               Objects.equals(price, that.price) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flight, cabinType, price, amount);
    }

    @Override
    public String toString() {
        return "TicketResponse{" +
               "id=" + id +
               ", flight=" + flight +
               ", cabinType=" + cabinType +
               ", price=" + price +
               ", amount=" + amount +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final TicketResponse ticketResponse;

        public Builder() {
            ticketResponse = new TicketResponse();
        }

        public Builder id(Long id) {
            this.ticketResponse.id = id;
            return this;
        }

        public Builder flight(FlightResponse flight) {
            this.ticketResponse.flight = flight;
            return this;
        }

        public Builder cabinType(CabinType cabinType) {
            this.ticketResponse.cabinType = cabinType;
            return this;
        }

        public Builder price(Long price) {
            this.ticketResponse.price = price;
            return this;
        }

        public Builder amount(Long amount) {
            this.ticketResponse.amount = amount;
            return this;
        }

        public TicketResponse build() {
            return this.ticketResponse;
        }
    }
}
