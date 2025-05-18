package org.burgas.hotelbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("ALL")
public final class RoomImagePK {

    private Long roomId;
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
        RoomImagePK that = (RoomImagePK) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, imageId);
    }

    @Override
    public String toString() {
        return "RoomImagePK{" +
               "roomId=" + roomId +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RoomImagePK roomImagePK;

        public Builder() {
            roomImagePK = new RoomImagePK();
        }

        public Builder roomId(Long roomId) {
            this.roomImagePK.roomId = roomId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.roomImagePK.imageId = imageId;
            return this;
        }

        public RoomImagePK build() {
            return this.roomImagePK;
        }
    }
}
