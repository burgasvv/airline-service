package org.burgas.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Airport {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Long addressId;
    private Boolean opened;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(id, airport.id) && Objects.equals(name, airport.name) && Objects.equals(addressId, airport.addressId) &&
               Objects.equals(opened, airport.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addressId, opened);
    }

    @Override
    public String toString() {
        return "Airport{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", addressId=" + addressId +
               ", opened=" + opened +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Airport airport;

        public Builder() {
            airport = new Airport();
        }

        public Builder id(Long id) {
            this.airport.id = id;
            return this;
        }

        public Builder name(String name) {
            this.airport.name = name;
            return this;
        }

        public Builder addressId(Long addressId) {
            this.airport.addressId = addressId;
            return this;
        }

        public Builder opened(Boolean opened) {
            this.airport.opened = opened;
            return this;
        }

        public Airport build() {
            return this.airport;
        }
    }
}
