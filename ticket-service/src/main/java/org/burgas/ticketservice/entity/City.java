package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class City implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private Long countryId;

    @Transient
    private Boolean isNew;

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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean isNew() {
        return isNew | id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id) && Objects.equals(name, city.name) &&
               Objects.equals(countryId, city.countryId) && Objects.equals(isNew, city.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryId, isNew);
    }

    @Override
    public String toString() {
        return "City{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", countryId=" + countryId +
               ", isNew=" + isNew +
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

        public Builder countryId(Long countryId) {
            this.city.countryId = countryId;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.city.isNew = isNew;
            return this;
        }

        public City build() {
            return this.city;
        }
    }
}
