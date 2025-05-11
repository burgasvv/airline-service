package org.burgas.excursionbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CountryResponse extends Response {

    private Long id;
    private String name;
    private String description;
    private Long population;
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

    public Long getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryResponse that = (CountryResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
               Objects.equals(population, that.population) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, population, imageId);
    }

    @Override
    public String toString() {
        return "CountryResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", population=" + population +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final CountryResponse countryResponse;

        public Builder() {
            countryResponse = new CountryResponse();
        }

        public Builder id(Long id) {
            this.countryResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.countryResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            this.countryResponse.description = description;
            return this;
        }

        public Builder population(Long population) {
            this.countryResponse.population = population;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.countryResponse.imageId = imageId;
            return this;
        }

        public CountryResponse build() {
            return this.countryResponse;
        }
    }
}
