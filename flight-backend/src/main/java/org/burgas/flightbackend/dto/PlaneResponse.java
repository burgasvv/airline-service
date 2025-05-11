package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class PlaneResponse extends Response {

    private Long id;
    private String number;
    private String model;
    private Long businessClass;
    private Long economyClass;
    private Boolean free;
    private Boolean inService;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getModel() {
        return model;
    }

    public Long getBusinessClass() {
        return businessClass;
    }

    public Long getEconomyClass() {
        return economyClass;
    }

    public Boolean getFree() {
        return free;
    }

    public Boolean getInService() {
        return inService;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlaneResponse that = (PlaneResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(model, that.model) &&
               Objects.equals(businessClass, that.businessClass) && Objects.equals(economyClass, that.economyClass) &&
               Objects.equals(free, that.free) && Objects.equals(inService, that.inService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, model, businessClass, economyClass, free, inService);
    }

    @Override
    public String toString() {
        return "PlaneResponse{" +
               "id=" + id +
               ", number='" + number + '\'' +
               ", model='" + model + '\'' +
               ", businessClass=" + businessClass +
               ", economyClass=" + economyClass +
               ", free=" + free +
               ", inService=" + inService +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final PlaneResponse planeResponse;

        public Builder() {
            planeResponse = new PlaneResponse();
        }

        public Builder id(Long id) {
            this.planeResponse.id = id;
            return this;
        }

        public Builder number(String number) {
            this.planeResponse.number = number;
            return this;
        }

        public Builder model(String model) {
            this.planeResponse.model = model;
            return this;
        }

        public Builder businessClass(Long businessClass) {
            this.planeResponse.businessClass = businessClass;
            return this;
        }

        public Builder economyClass(Long economyClass) {
            this.planeResponse.economyClass = economyClass;
            return this;
        }

        public Builder free(Boolean free) {
            this.planeResponse.free = free;
            return this;
        }

        public Builder inService(Boolean inService) {
            this.planeResponse.inService = inService;
            return this;
        }

        public PlaneResponse build() {
            return this.planeResponse;
        }
    }
}
