package org.burgas.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

import static jakarta.persistence.GenerationType.*;

@Entity
@SuppressWarnings("ALL")
public final class CabinType {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CabinType cabinType = (CabinType) o;
        return Objects.equals(id, cabinType.id) && Objects.equals(name, cabinType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "CabinType{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final CabinType cabinType;

        public Builder() {
            cabinType = new CabinType();
        }

        public Builder id(Long id) {
            this.cabinType.id = id;
            return this;
        }

        public Builder name(String name) {
            this.cabinType.name = name;
            return this;
        }

        public CabinType build() {
            return this.cabinType;
        }
    }
}
