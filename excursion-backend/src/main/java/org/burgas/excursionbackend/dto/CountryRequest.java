package org.burgas.excursionbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CountryRequest extends Request {

    private Long id;
    private String name;
    private String description;
    private Long population;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryRequest that = (CountryRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
               Objects.equals(population, that.population);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, population);
    }
}
