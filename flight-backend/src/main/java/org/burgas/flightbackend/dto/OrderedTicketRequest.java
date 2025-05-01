package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class OrderedTicketRequest {

    private Long id;
    private Long identityId;
    private Long ticketId;
    private Long flightSeatId;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderedTicketRequest that = (OrderedTicketRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(identityId, that.identityId) &&
               Objects.equals(ticketId, that.ticketId) && Objects.equals(flightSeatId, that.flightSeatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityId, ticketId, flightSeatId);
    }

    @Override
    public String toString() {
        return "OrderedTicketRequest{" +
               "id=" + id +
               ", identityId=" + identityId +
               ", ticketId=" + ticketId +
               ", flightSeatId=" + flightSeatId +
               '}';
    }
}
