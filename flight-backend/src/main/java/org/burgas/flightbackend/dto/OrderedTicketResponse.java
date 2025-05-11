package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class OrderedTicketResponse extends Response {

    private Long id;
    private IdentityResponse identity;
    private TicketResponse ticket;
    private FlightSeatResponse flightSeat;
    private Boolean closed;

    public Long getId() {
        return id;
    }

    public IdentityResponse getIdentity() {
        return identity;
    }

    public TicketResponse getTicket() {
        return ticket;
    }

    public FlightSeatResponse getFlightSeat() {
        return flightSeat;
    }

    public Boolean getClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderedTicketResponse that = (OrderedTicketResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(identity, that.identity) && Objects.equals(ticket, that.ticket) &&
               Objects.equals(flightSeat, that.flightSeat) && Objects.equals(closed, that.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identity, ticket, flightSeat, closed);
    }

    @Override
    public String toString() {
        return "OrderedTicketResponse{" +
               "id=" + id +
               ", identity=" + identity +
               ", ticket=" + ticket +
               ", flightSeat=" + flightSeat +
               ", closed=" + closed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final OrderedTicketResponse orderedTicketResponse;

        public Builder() {
            orderedTicketResponse = new OrderedTicketResponse();
        }

        public Builder id(Long id) {
            this.orderedTicketResponse.id = id;
            return this;
        }

        public Builder identity(IdentityResponse identity) {
            this.orderedTicketResponse.identity = identity;
            return this;
        }

        public Builder ticket(TicketResponse ticket) {
            this.orderedTicketResponse.ticket = ticket;
            return this;
        }

        public Builder flightSeat(FlightSeatResponse flightSeat) {
            this.orderedTicketResponse.flightSeat = flightSeat;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.orderedTicketResponse.closed = closed;
            return this;
        }

        public OrderedTicketResponse build() {
            return this.orderedTicketResponse;
        }
    }
}
