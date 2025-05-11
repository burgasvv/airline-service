package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CountryResponse extends Response {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryResponse that = (CountryResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "CountryResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
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

        public CountryResponse build() {
            return this.countryResponse;
        }
    }
}
