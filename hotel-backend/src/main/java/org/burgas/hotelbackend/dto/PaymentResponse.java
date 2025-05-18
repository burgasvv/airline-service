package org.burgas.hotelbackend.dto;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class PaymentResponse {

    private Long id;
    private ClientResponse client;
    private FilialResponse filial;
    private List<RoomResponse> rooms;
    private Long cost;
    private Boolean closed;
    private Boolean cancelled;

    public Long getId() {
        return id;
    }

    public ClientResponse getClient() {
        return client;
    }

    public FilialResponse getFilial() {
        return filial;
    }

    public List<RoomResponse> getRooms() {
        return rooms;
    }

    public Long getCost() {
        return cost;
    }

    public Boolean getClosed() {
        return closed;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResponse that = (PaymentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(client, that.client) && Objects.equals(filial, that.filial) &&
               Objects.equals(rooms, that.rooms) && Objects.equals(cost, that.cost) && Objects.equals(closed, that.closed) &&
               Objects.equals(cancelled, that.cancelled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, filial, rooms, cost, closed, cancelled);
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
               "id=" + id +
               ", client=" + client +
               ", filial=" + filial +
               ", rooms=" + rooms +
               ", cost=" + cost +
               ", closed=" + closed +
               ", cancelled=" + cancelled +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final PaymentResponse paymentResponse;

        public Builder() {
            paymentResponse = new PaymentResponse();
        }

        public Builder id(Long id) {
            this.paymentResponse.id = id;
            return this;
        }

        public Builder client(ClientResponse client) {
            this.paymentResponse.client = client;
            return this;
        }

        public Builder filial(FilialResponse filial) {
            this.paymentResponse.filial = filial;
            return this;
        }

        public Builder rooms(List<RoomResponse> rooms) {
            this.paymentResponse.rooms = rooms;
            return this;
        }

        public Builder cost(Long cost) {
            this.paymentResponse.cost = cost;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.paymentResponse.closed = closed;
            return this;
        }

        public Builder cancelled(Boolean cancelled) {
            this.paymentResponse.cancelled = cancelled;
            return this;
        }

        public PaymentResponse build() {
            return this.paymentResponse;
        }
    }
}
