package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Country implements Persistable<Long> {

    @Id
    private Long id;
    private String name;

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

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id) && Objects.equals(name, country.name) && Objects.equals(isNew, country.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isNew);
    }

    @Override
    public String toString() {
        return "Country{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", isNew=" + isNew +
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

        public Builder isNew(Boolean isNew) {
            this.country.isNew = isNew;
            return this;
        }

        public Country build() {
            return this.country;
        }
    }
}
