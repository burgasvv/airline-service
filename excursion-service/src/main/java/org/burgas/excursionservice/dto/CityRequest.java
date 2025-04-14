package org.burgas.excursionservice.dto;

@SuppressWarnings("unused")
public final class CityRequest {

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
}
