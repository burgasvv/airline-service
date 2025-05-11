package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class PlaneRequest extends Request {

    private Long id;
    private String model;
    private Long businessClass;
    private Long economyClass;
    private Boolean free;
    private Boolean inService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(Long businessClass) {
        this.businessClass = businessClass;
    }

    public Long getEconomyClass() {
        return economyClass;
    }

    public void setEconomyClass(Long economyClass) {
        this.economyClass = economyClass;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean getInService() {
        return inService;
    }

    public void setInService(Boolean inService) {
        this.inService = inService;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlaneRequest that = (PlaneRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(model, that.model) && Objects.equals(businessClass, that.businessClass) &&
               Objects.equals(economyClass, that.economyClass) && Objects.equals(free, that.free) && Objects.equals(inService, that.inService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, businessClass, economyClass, free, inService);
    }

    @Override
    public String toString() {
        return "PlaneRequest{" +
               "id=" + id +
               ", model='" + model + '\'' +
               ", businessClass=" + businessClass +
               ", economyClass=" + economyClass +
               ", free=" + free +
               ", inService=" + inService +
               '}';
    }
}
