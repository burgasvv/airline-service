package org.burgas.excursionservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings(value = "unused")
public final class Authority implements Serializable {

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Authority authority;

        public Builder() {
            authority = new Authority();
        }

        public Builder id(Long id) {
            this.authority.id = id;
            return this;
        }

        public Builder name(String name) {
            this.authority.name = name;
            return this;
        }

        public Authority build() {
            return this.authority;
        }
    }
}
