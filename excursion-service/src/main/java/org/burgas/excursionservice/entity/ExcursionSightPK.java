package org.burgas.excursionservice.entity;

import jakarta.persistence.Embeddable;

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
