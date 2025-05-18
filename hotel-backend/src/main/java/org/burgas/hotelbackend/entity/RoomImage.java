package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Objects;

@Entity
@IdClass(value = RoomImagePK.class)
@SuppressWarnings("ALL")
public final class RoomImage {

    @Id
    private Long roomId;

    @Id
    private Long imageId;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomImage roomImage = (RoomImage) o;
        return Objects.equals(roomId, roomImage.roomId) && Objects.equals(imageId, roomImage.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, imageId);
    }

    @Override
    public String toString() {
        return "RoomImage{" +
               "roomId=" + roomId +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RoomImage roomImage;

        public Builder() {
            roomImage = new RoomImage();
        }

        public Builder roomId(Long roomId) {
            this.roomImage.roomId = roomId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.roomImage.imageId = imageId;
            return this;
        }

        public RoomImage build() {
            return this.roomImage;
        }
    }
}
