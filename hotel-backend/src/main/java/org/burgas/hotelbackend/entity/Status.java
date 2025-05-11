package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Status extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(id, status.id) && Objects.equals(rating, status.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating);
    }

    @Override
    public String toString() {
        return "Status{" +
               "id=" + id +
               ", rating='" + rating + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Status status;

        public Builder() {
            status = new Status();
        }

        public Builder id(Long id) {
            this.status.id = id;
            return this;
        }

        public Builder rating(String rating) {
            this.status.rating = rating;
            return this;
        }

        public Status build() {
            return this.status;
        }
    }
}
