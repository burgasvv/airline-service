package org.burgas.excursionbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CityRequest extends Request {

    private Long id;
    private String name;
    private String description;
    private Long population;
    private Long countryId;
    private Boolean capital;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CityRequest that = (CityRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) &&
               Objects.equals(population, that.population) && Objects.equals(countryId, that.countryId) && Objects.equals(capital, that.capital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, population, countryId, capital);
    }
}
