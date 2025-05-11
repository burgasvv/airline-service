package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class City extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long population;
    private Long countryId;
    private Boolean capital;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Boolean getCapital() {
        return capital;
    }

    public void setCapital(Boolean capital) {
        this.capital = capital;
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
        City city = (City) o;
        return Objects.equals(id, city.id) && Objects.equals(name, city.name) &&
               Objects.equals(description, city.description) && Objects.equals(population, city.population) &&
               Objects.equals(countryId, city.countryId) && Objects.equals(capital, city.capital) && Objects.equals(imageId, city.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, population, countryId, capital, imageId);
    }

    @Override
    public String toString() {
        return "City{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", population=" + population +
               ", countryId=" + countryId +
               ", capital=" + capital +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final City city;

        public Builder() {
            city = new City();
        }

        public Builder id(Long id) {
            this.city.id = id;
            return this;
        }

        public Builder name(String name) {
            this.city.name = name;
            return this;
        }

        public Builder description(String description) {
            this.city.description = description;
            return this;
        }

        public Builder population(Long population) {
            this.city.population = population;
            return this;
        }

        public Builder countryId(Long countryId) {
            this.city.countryId = countryId;
            return this;
        }

        public Builder capital(Boolean capital) {
            this.city.capital = capital;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.city.imageId = imageId;
            return this;
        }

        public City build() {
            return this.city;
        }
    }
}
