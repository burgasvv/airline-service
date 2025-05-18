package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(value = ClientRoomPK.class)
@SuppressWarnings("ALL")
public final class ClientRoom extends AbstractEntity implements Serializable {

    @Id
    private Long clientId;

    @Id
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
        ClientRoom that = (ClientRoom) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, roomId);
    }

    @Override
    public String toString() {
        return "ClientRoom{" +
               "clientId=" + clientId +
               ", roomId=" + roomId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ClientRoom clientRoom;

        public Builder() {
            clientRoom = new ClientRoom();
        }

        public Builder clientId(Long clientId) {
            this.clientRoom.clientId = clientId;
            return this;
        }

        public Builder roomId(Long roomId) {
            this.clientRoom.roomId = roomId;
            return this;
        }

        public ClientRoom build() {
            return this.clientRoom;
        }
    }
}
