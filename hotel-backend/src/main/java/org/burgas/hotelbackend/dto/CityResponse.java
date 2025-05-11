package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class CityResponse extends Response {

    private Long id;
    private String name;
    private String description;
    private CountryResponse country;
    private Boolean capital;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CountryResponse getCountry() {
        return country;
    }

    public Boolean getCapital() {
        return capital;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CityResponse that = (CityResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
               Objects.equals(country, that.country) && Objects.equals(capital, that.capital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, country, capital);
    }

    @Override
    public String toString() {
        return "CityResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", country=" + country +
               ", capital=" + capital +
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

        public Builder description(String description) {
            this.cityResponse.description = description;
            return this;
        }

        public Builder country(CountryResponse country) {
            this.cityResponse.country = country;
            return this;
        }

        public Builder capital(Boolean capital) {
            this.cityResponse.capital = capital;
            return this;
        }

        public CityResponse build() {
            return this.cityResponse;
        }
    }
}
