package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Authority implements Persistable<Long> {

    @Id
    private Long id;
    private String name;

    @Transient
    private Boolean isNew;

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
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(id, authority.id) && Objects.equals(name, authority.name) && Objects.equals(isNew, authority.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isNew);
    }

    @Override
    public String toString() {
        return "Authority{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", isNew=" + isNew +
               '}';
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

        public Builder isNew(Boolean isNew) {
            this.authority.isNew = isNew;
            return this;
        }

        public Authority build() {
            return this.authority;
        }
    }
}
