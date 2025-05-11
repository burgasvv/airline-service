package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class OrderedTicket extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long identityId;
    private Long ticketId;
    private Long flightSeatId;
    private Boolean closed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getFlightSeatId() {
        return flightSeatId;
    }

    public void setFlightSeatId(Long flightSeatId) {
        this.flightSeatId = flightSeatId;
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
        OrderedTicket that = (OrderedTicket) o;
        return Objects.equals(id, that.id) && Objects.equals(identityId, that.identityId) &&
               Objects.equals(ticketId, that.ticketId) && Objects.equals(flightSeatId, that.flightSeatId) && Objects.equals(closed, that.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityId, ticketId, flightSeatId, closed);
    }

    @Override
    public String toString() {
        return "OrderedTicket{" +
               "id=" + id +
               ", identityId=" + identityId +
               ", ticketId=" + ticketId +
               ", flightSeatId=" + flightSeatId +
               ", closed=" + closed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final OrderedTicket orderedTicket;

        public Builder() {
            orderedTicket = new OrderedTicket();
        }

        public Builder id(Long id) {
            this.orderedTicket.id = id;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.orderedTicket.identityId = identityId;
            return this;
        }

        public Builder ticketId(Long ticketId) {
            this.orderedTicket.ticketId = ticketId;
            return this;
        }

        public Builder flightSeatId(Long flightSeatId) {
            this.orderedTicket.flightSeatId = flightSeatId;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.orderedTicket.closed = closed;
            return this;
        }

        public OrderedTicket build() {
            return this.orderedTicket;
        }
    }
}
