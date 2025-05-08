package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class HotelRequest {

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
        HotelRequest that = (HotelRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(statusId, that.statusId) &&
               Objects.equals(description, that.description) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, statusId, description, imageId);
    }

    @Override
    public String toString() {
        return "HotelRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", statusId=" + statusId +
               ", description='" + description + '\'' +
               ", imageId=" + imageId +
               '}';
    }
}
