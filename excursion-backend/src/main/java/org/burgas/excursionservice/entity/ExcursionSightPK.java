package org.burgas.excursionservice.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("ALL")
public final class ExcursionSightPK {

    private Long excursionId;
    private Long sightId;

    public Long getExcursionId() {
        return excursionId;
    }

    public Long getSightId() {
        return sightId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcursionSightPK that = (ExcursionSightPK) o;
        return Objects.equals(excursionId, that.excursionId) && Objects.equals(sightId, that.sightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excursionId, sightId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ExcursionSightPK excursionSightPK;

        public Builder() {
            excursionSightPK = new ExcursionSightPK();
        }

        public Builder excursionId(Long excursionId) {
            this.excursionSightPK.excursionId = excursionId;
            return this;
        }

        public Builder sightId(Long sightId) {
            this.excursionSightPK.sightId = sightId;
            return this;
        }

        public ExcursionSightPK buid() {
            return this.excursionSightPK;
        }
    }
}
