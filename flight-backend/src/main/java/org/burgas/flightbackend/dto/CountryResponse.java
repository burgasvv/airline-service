package org.burgas.flightbackend.dto;

@SuppressWarnings("unused")
public final class CountryResponse {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
