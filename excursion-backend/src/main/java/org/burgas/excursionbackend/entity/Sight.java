package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Sight extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long cityId;
    private Long imageId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sight sight = (Sight) o;
        return Objects.equals(id, sight.id) && Objects.equals(name, sight.name) &&
               Objects.equals(description, sight.description) && Objects.equals(cityId, sight.cityId) && Objects.equals(imageId, sight.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, cityId, imageId);
    }

    @Override
    public String toString() {
        return "Sight{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", cityId=" + cityId +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Sight sight;

        public Builder() {
            sight = new Sight();
        }

        public Builder id(Long id) {
            this.sight.id = id;
            return this;
        }

        public Builder name(String name) {
            this.sight.name = name;
            return this;
        }

        public Builder description(String description) {
            this.sight.description = description;
            return this;
        }

        public Builder cityId(Long cityId) {
            this.sight.cityId = cityId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.sight.imageId = imageId;
            return this;
        }

        public Sight build() {
            return this.sight;
        }
    }
}
