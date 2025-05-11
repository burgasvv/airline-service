package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Hotel extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Long statusId;
    private String description;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) && Objects.equals(name, hotel.name) && Objects.equals(statusId, hotel.statusId) &&
               Objects.equals(description, hotel.description) && Objects.equals(imageId, hotel.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, statusId, description, imageId);
    }

    @Override
    public String toString() {
        return "Hotel{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", statusId=" + statusId +
               ", description='" + description + '\'' +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Hotel hotel;

        public Builder() {
            hotel = new Hotel();
        }

        public Builder id(Long id) {
            this.hotel.id = id;
            return this;
        }

        public Builder name(String name) {
            this.hotel.name = name;
            return this;
        }

        public Builder statusId(Long statusId) {
            this.hotel.statusId = statusId;
            return this;
        }

        public Builder description(String description) {
            this.hotel.description = description;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.hotel.imageId = imageId;
            return this;
        }

        public Hotel build() {
            return this.hotel;
        }
    }
}
