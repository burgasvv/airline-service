package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Filial extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Long addressId;
    private LocalTime opensAt;
    private LocalTime closesAt;
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

    public LocalTime getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(LocalTime opensAt) {
        this.opensAt = opensAt;
    }

    public LocalTime getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(LocalTime closesAt) {
        this.closesAt = closesAt;
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
        Filial filial = (Filial) o;
        return Objects.equals(id, filial.id) && Objects.equals(name, filial.name) && Objects.equals(addressId, filial.addressId) &&
               Objects.equals(opensAt, filial.opensAt) && Objects.equals(closesAt, filial.closesAt) && Objects.equals(opened, filial.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addressId, opensAt, closesAt, opened);
    }

    @Override
    public String toString() {
        return "Filial{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", addressId=" + addressId +
               ", opensAt=" + opensAt +
               ", closesAt=" + closesAt +
               ", opened=" + opened +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Filial filial;

        public Builder() {
            filial = new Filial();
        }

        public Builder id(Long id) {
            this.filial.id = id;
            return this;
        }

        public Builder name(String name) {
            this.filial.name = name;
            return this;
        }

        public Builder addressId(Long addressId) {
            this.filial.addressId = addressId;
            return this;
        }

        public Builder opensAt(LocalTime opensAt) {
            this.filial.opensAt = opensAt;
            return this;
        }

        public Builder closesAt(LocalTime closesAt) {
            this.filial.closesAt = closesAt;
            return this;
        }

        public Builder opened(Boolean opened) {
            this.filial.opened = opened;
            return this;
        }

        public Filial build() {
            return this.filial;
        }
    }
}
