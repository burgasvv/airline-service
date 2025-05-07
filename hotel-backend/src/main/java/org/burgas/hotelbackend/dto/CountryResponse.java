package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class CountryResponse {

    private Long id;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryResponse that = (CountryResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "CountryResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
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

        public CountryResponse build() {
            return this.countryResponse;
        }
    }
}
