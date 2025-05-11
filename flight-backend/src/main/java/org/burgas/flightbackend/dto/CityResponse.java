package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CityResponse extends Response {

    private Long id;
    private String name;
    private CountryResponse country;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CountryResponse getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CityResponse that = (CityResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country);
    }

    @Override
    public String toString() {
        return "CityResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", country=" + country +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final CityResponse cityResponse;

        public Builder() {
            cityResponse = new CityResponse();
        }

        public Builder id(Long id) {
            this.cityResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.cityResponse.name = name;
            return this;
        }

        public Builder country(CountryResponse country) {
            this.cityResponse.country = country;
            return this;
        }

        public CityResponse build() {
            return this.cityResponse;
        }
    }
}
