package org.burgas.excursionservice.dto;

@SuppressWarnings("unused")
public final class SightResponse {

    private Long id;
    private String name;
    private String description;
    private CityResponse city;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CityResponse getCity() {
        return city;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        return "SightResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", city=" + city +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final SightResponse sightResponse;

        public Builder() {
            sightResponse = new SightResponse();
        }

        public Builder id(Long id) {
            this.sightResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.sightResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            this.sightResponse.description = description;
            return this;
        }

        public Builder city(CityResponse city) {
            this.sightResponse.city = city;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.sightResponse.imageId = imageId;
            return this;
        }

        public SightResponse build() {
            return this.sightResponse;
        }
    }
}
