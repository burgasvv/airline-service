package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Objects;

@Entity
@IdClass(value = ExcursionSightPK.class)
@SuppressWarnings("ALL")
public final class ExcursionSight {

    @Id
    private Long excursionId;

    @Id
    private Long sightId;

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public Long getSightId() {
        return sightId;
    }

    public void setSightId(Long sightId) {
        this.sightId = sightId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcursionSight that = (ExcursionSight) o;
        return Objects.equals(excursionId, that.excursionId) && Objects.equals(sightId, that.sightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excursionId, sightId);
    }

    @Override
    public String toString() {
        return "ExcursionSight{" +
               "excursionId=" + excursionId +
               ", sightId=" + sightId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ExcursionSight excursionSight;

        public Builder() {
            excursionSight = new ExcursionSight();
        }

        public Builder excursionId(Long excursionId) {
            this.excursionSight.excursionId = excursionId;
            return this;
        }

        public Builder sightId(Long sightId) {
            this.excursionSight.sightId = sightId;
            return this;
        }

        public ExcursionSight build() {
            return this.excursionSight;
        }
    }
}
