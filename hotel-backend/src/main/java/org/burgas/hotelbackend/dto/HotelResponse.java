package org.burgas.hotelbackend.dto;

import org.burgas.hotelbackend.entity.Status;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class HotelResponse extends Response {

    private Long id;
    private String name;
    private Status status;
    private String description;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HotelResponse that = (HotelResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(status, that.status) &&
               Objects.equals(description, that.description) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, imageId);
    }

    @Override
    public String toString() {
        return "HotelResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", status=" + status +
               ", description='" + description + '\'' +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final HotelResponse hotelResponse;

        public Builder() {
            hotelResponse = new HotelResponse();
        }

        public Builder id(Long id) {
            this.hotelResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.hotelResponse.name = name;
            return this;
        }

        public Builder status(Status status) {
            this.hotelResponse.status = status;
            return this;
        }

        public Builder description(String description) {
            this.hotelResponse.description = description;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.hotelResponse.imageId = imageId;
            return this;
        }

        public HotelResponse build() {
            return this.hotelResponse;
        }
    }
}
