package org.burgas.ticketservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Image implements Persistable<Long> {

    @Id
    private Long id;
    private String name;

    @JsonIgnore
    private byte[] data;

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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(name, image.name) &&
               Objects.deepEquals(data, image.data) && Objects.equals(isNew, image.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, Arrays.hashCode(data), isNew);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Image image;

        public Builder() {
            image = new Image();
        }

        public Builder id(Long id) {
            this.image.id = id;
            return this;
        }

        public Builder name(String name) {
            this.image.name = name;
            return this;
        }

        public Builder data(byte[] data) {
            this.image.data = data;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.image.isNew = isNew;
            return this;
        }

        public Image build() {
            return this.image;
        }
    }
}
