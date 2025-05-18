package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Payment extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long clientId;
    private Long filialId;
    private Long[] rooms;
    private Long cost;
    private Boolean closed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getFilialId() {
        return filialId;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public Long[] getRooms() {
        return rooms;
    }

    public void setRooms(Long[] rooms) {
        this.rooms = rooms;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
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
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(clientId, payment.clientId) && Objects.equals(filialId, payment.filialId) &&
               Objects.deepEquals(rooms, payment.rooms) && Objects.equals(cost, payment.cost) && Objects.equals(closed, payment.closed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, filialId, Arrays.hashCode(rooms), cost, closed);
    }

    @Override
    public String toString() {
        return "Payment{" +
               "id=" + id +
               ", clientId=" + clientId +
               ", filialId=" + filialId +
               ", rooms=" + Arrays.toString(rooms) +
               ", cost=" + cost +
               ", closed=" + closed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Payment payment;

        public Builder() {
            payment = new Payment();
        }

        public Builder id(Long id) {
            this.payment.id = id;
            return this;
        }

        public Builder clientId(Long clientId) {
            this.payment.clientId = clientId;
            return this;
        }

        public Builder filialId(Long filialId) {
            this.payment.filialId = filialId;
            return this;
        }

        public Builder rooms(Long[] rooms) {
            this.payment.rooms = rooms;
            return this;
        }

        public Builder cost(Long cost) {
            this.payment.cost = cost;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.payment.closed = closed;
            return this;
        }

        public Payment build() {
            return this.payment;
        }
    }
}
