package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class AirportRequest extends Request {

    private Long id;
    private String name;
    private AddressRequest address;
    private Boolean opened;

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

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AirportRequest that = (AirportRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(opened, that.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, opened);
    }

    @Override
    public String toString() {
        return "AirportRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address=" + address +
               ", opened=" + opened +
               '}';
    }
}
