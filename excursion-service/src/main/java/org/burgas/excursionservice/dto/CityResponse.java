package org.burgas.excursionservice.dto;

@SuppressWarnings("unused")
public final class CityResponse {

    private Long id;
    private String name;
    private String description;
    private Long population;
    private CountryResponse country;
    private Boolean capital;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getPopulation() {
        return population;
    }

    public CountryResponse getCountry() {
        return country;
    }

    public Boolean getCapital() {
        return capital;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        return "CityResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", population=" + population +
               ", country=" + country +
               ", capital=" + capital +
               ", imageId=" + imageId +
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

        public Builder population(Long population) {
            this.cityResponse.population = population;
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

        public Builder imageId(Long imageId) {
            this.cityResponse.imageId = imageId;
            return this;
        }

        public CityResponse build() {
            return this.cityResponse;
        }
    }
}
