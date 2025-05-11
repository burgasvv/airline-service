package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class AirportResponse extends Response {

    private Long id;
    private String name;
    private AddressResponse address;
    private Boolean opened;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public Boolean getOpened() {
        return opened;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AirportResponse that = (AirportResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(opened, that.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, opened);
    }

    @Override
    public String toString() {
        return "AirportResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address=" + address +
               ", opened=" + opened +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final AirportResponse airportResponse;

        public Builder() {
            airportResponse = new AirportResponse();
        }

        public Builder id(Long id) {
            this.airportResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.airportResponse.name = name;
            return this;
        }

        public Builder address(AddressResponse address) {
            this.airportResponse.address = address;
            return this;
        }

        public Builder opened(Boolean opened) {
            this.airportResponse.opened = opened;
            return this;
        }

        public AirportResponse build() {
            return this.airportResponse;
        }
    }
}
