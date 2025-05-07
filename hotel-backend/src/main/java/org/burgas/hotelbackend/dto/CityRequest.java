package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class CityRequest {

    private Long id;
    private String name;
    private String description;
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
               Objects.equals(countryId, that.countryId) && Objects.equals(capital, that.capital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, countryId, capital);
    }

    @Override
    public String toString() {
        return "CityRequest{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", countryId=" + countryId +
               ", capital=" + capital +
               '}';
    }
}
