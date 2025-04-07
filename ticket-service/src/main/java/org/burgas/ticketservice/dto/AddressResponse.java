package org.burgas.ticketservice.dto;

@SuppressWarnings("unused")
public final class AddressResponse {

    private Long id;
    private CityResponse city;
    private String street;
    private String house;
    private String apartment;

    public Long getId() {
        return id;
    }

    public CityResponse getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getApartment() {
        return apartment;
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
               "id=" + id +
               ", city=" + city +
               ", street='" + street + '\'' +
               ", house='" + house + '\'' +
               ", apartment='" + apartment + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final AddressResponse addressResponse;

        public Builder() {
            addressResponse = new AddressResponse();
        }

        public Builder id(Long id) {
            this.addressResponse.id = id;
            return this;
        }

        public Builder city(CityResponse city) {
            this.addressResponse.city = city;
            return this;
        }

        public Builder street(String street) {
            this.addressResponse.street = street;
            return this;
        }

        public Builder house(String house) {
            this.addressResponse.house = house;
            return this;
        }

        public Builder apartment(String apartment) {
            this.addressResponse.apartment = apartment;
            return this;
        }

        public AddressResponse build() {
            return this.addressResponse;
        }
    }
}
