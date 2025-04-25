package org.burgas.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Ticket {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long flightId;
    private Long cabinTypeId;
    private Long price;
    private Long amount;

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

    public Long getCabinTypeId() {
        return cabinTypeId;
    }

    public void setCabinTypeId(Long cabinTypeId) {
        this.cabinTypeId = cabinTypeId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(flightId, ticket.flightId) &&
               Objects.equals(cabinTypeId, ticket.cabinTypeId) && Objects.equals(price, ticket.price) && Objects.equals(amount, ticket.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightId, cabinTypeId, price, amount);
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", flightId=" + flightId +
               ", cabinTypeId=" + cabinTypeId +
               ", price=" + price +
               ", amount=" + amount +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Ticket ticket;

        public Builder() {
            ticket = new Ticket();
        }

        public Builder id(Long id) {
            this.ticket.id = id;
            return this;
        }

        public Builder flightId(Long flightId) {
            this.ticket.flightId = flightId;
            return this;
        }

        public Builder cabinTypeId(Long cabinTypeId) {
            this.ticket.cabinTypeId = cabinTypeId;
            return this;
        }

        public Builder price(Long price) {
            this.ticket.price = price;
            return this;
        }

        public Builder amount(Long amount) {
            this.ticket.amount = amount;
            return this;
        }

        public Ticket build() {
            return this.ticket;
        }
    }
}
