package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Plane {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        Plane plane = (Plane) o;
        return Objects.equals(id, plane.id) && Objects.equals(number, plane.number) && Objects.equals(model, plane.model) &&
               Objects.equals(businessClass, plane.businessClass) && Objects.equals(economyClass, plane.economyClass) &&
               Objects.equals(free, plane.free) && Objects.equals(inService, plane.inService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, model, businessClass, economyClass, free, inService);
    }

    @Override
    public String toString() {
        return "Plane{" +
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

        private final Plane plane;

        public Builder() {
            plane = new Plane();
        }

        public Builder id(Long id) {
            this.plane.id = id;
            return this;
        }

        public Builder number(String number) {
            this.plane.number = number;
            return this;
        }

        public Builder model(String model) {
            this.plane.model = model;
            return this;
        }

        public Builder businessClass(Long businessClass) {
            this.plane.businessClass = businessClass;
            return this;
        }

        public Builder economyClass(Long economyClass) {
            this.plane.economyClass = economyClass;
            return this;
        }

        public Builder free(Boolean free) {
            this.plane.free = free;
            return this;
        }

        public Builder inService(Boolean inService) {
            this.plane.inService = inService;
            return this;
        }

        public Plane build() {
            return this.plane;
        }
    }
}
