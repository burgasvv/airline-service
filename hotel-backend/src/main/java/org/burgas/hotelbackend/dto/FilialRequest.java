package org.burgas.hotelbackend.dto;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class FilialRequest extends Request {

    private Long id;
    private Long hotelId;
    private AddressRequest address;
    private Long luxRooms;
    private Long economyRooms;
    private Long imageId;
    private List<Long> departmentIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public Long getLuxRooms() {
        return luxRooms;
    }

    public void setLuxRooms(Long luxRooms) {
        this.luxRooms = luxRooms;
    }

    public Long getEconomyRooms() {
        return economyRooms;
    }

    public void setEconomyRooms(Long economyRooms) {
        this.economyRooms = economyRooms;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialRequest that = (FilialRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(hotelId, that.hotelId) && Objects.equals(address, that.address) &&
               Objects.equals(luxRooms, that.luxRooms) && Objects.equals(economyRooms, that.economyRooms) && Objects.equals(imageId, that.imageId) &&
               Objects.equals(departmentIds, that.departmentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hotelId, address, luxRooms, economyRooms, imageId, departmentIds);
    }

    @Override
    public String toString() {
        return "FilialRequest{" +
               "id=" + id +
               ", hotelId=" + hotelId +
               ", address=" + address +
               ", luxRooms=" + luxRooms +
               ", economyRooms=" + economyRooms +
               ", imageId=" + imageId +
               ", departmentIds=" + departmentIds +
               '}';
    }
}
