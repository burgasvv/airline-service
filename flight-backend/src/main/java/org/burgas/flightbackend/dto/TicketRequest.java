package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class TicketRequest {

    private Long id;
    private Long flightId;
    private Long cabinTypeId;
    private Long price;
    private Long amount;
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequest that = (TicketRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(flightId, that.flightId) &&
               Objects.equals(cabinTypeId, that.cabinTypeId) && Objects.equals(price, that.price) &&
               Objects.equals(amount, that.amount) && Objects.equals(closed, that.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightId, cabinTypeId, price, amount, closed);
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
               "id=" + id +
               ", flightId=" + flightId +
               ", cabinTypeId=" + cabinTypeId +
               ", price=" + price +
               ", amount=" + amount +
               ", closed=" + closed +
               '}';
    }
}
