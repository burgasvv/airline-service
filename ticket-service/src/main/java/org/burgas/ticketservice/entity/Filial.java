package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalTime;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Filial implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private Long addressId;
    private LocalTime opensAt;
    private LocalTime closesAt;
    private Boolean opened;

    @Transient
    public Boolean isNew;

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
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Filial filial = (Filial) o;
        return Objects.equals(id, filial.id) && Objects.equals(name, filial.name) &&
               Objects.equals(addressId, filial.addressId) && Objects.equals(opensAt, filial.opensAt) &&
               Objects.equals(closesAt, filial.closesAt) && Objects.equals(opened, filial.opened) && Objects.equals(isNew, filial.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addressId, opensAt, closesAt, opened, isNew);
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
               ", isNew=" + isNew +
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

        public Builder isNew(Boolean isNew) {
            this.filial.isNew = isNew;
            return this;
        }

        public Filial build() {
            return this.filial;
        }
    }
}
