package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Country {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long population;
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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id) && Objects.equals(name, country.name) &&
               Objects.equals(description, country.description) && Objects.equals(population, country.population) &&
               Objects.equals(imageId, country.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, population, imageId);
    }

    @Override
    public String toString() {
        return "Country{" +
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

        private final Country country;

        public Builder() {
            country = new Country();
        }

        public Builder id(Long id) {
            this.country.id = id;
            return this;
        }

        public Builder name(String name) {
            this.country.name = name;
            return this;
        }

        public Builder description(String description) {
            this.country.description = description;
            return this;
        }

        public Builder population(Long population) {
            this.country.population = population;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.country.imageId = imageId;
            return this;
        }

        public Country build() {
            return this.country;
        }
    }
}
