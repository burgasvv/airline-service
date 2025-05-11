package org.burgas.flightbackend.dto;

import java.time.LocalTime;
import java.util.Objects;

@SuppressWarnings("unused")
public final class FilialRequest extends Request {

    private Long id;
    private String name;
    private AddressRequest address;
    private LocalTime opensAt;
    private LocalTime closesAt;
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

    public LocalTime getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(LocalTime opensAt) {
        this.opensAt = opensAt;
    }

    public LocalTime getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(LocalTime closesAt) {
        this.closesAt = closesAt;
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
        FilialRequest that = (FilialRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) &&
               Objects.equals(opensAt, that.opensAt) && Objects.equals(closesAt, that.closesAt) && Objects.equals(opened, that.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, opensAt, closesAt, opened);
    }

    @Override
    public String toString() {
        return "FilialRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address=" + address +
               ", opensAt=" + opensAt +
               ", closesAt=" + closesAt +
               ", opened=" + opened +
               '}';
    }
}
