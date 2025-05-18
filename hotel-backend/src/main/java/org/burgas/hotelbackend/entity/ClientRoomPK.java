package org.burgas.hotelbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@SuppressWarnings("ALL")
@Embeddable
public final class ClientRoomPK {

    private Long clientId;
    private Long roomId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientRoomPK that = (ClientRoomPK) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, roomId);
    }

    @Override
    public String toString() {
        return "ClientRoomPK{" +
               "clientId=" + clientId +
               ", roomId=" + roomId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ClientRoomPK clientRoomPK;

        public Builder() {
            clientRoomPK = new ClientRoomPK();
        }

        public Builder clientId(Long clientId) {
            this.clientRoomPK.clientId = clientId;
            return this;
        }

        public Builder roomId(Long roomId) {
            this.clientRoomPK.roomId = roomId;
            return this;
        }

        public ClientRoomPK build() {
            return this.clientRoomPK;
        }
    }
}
