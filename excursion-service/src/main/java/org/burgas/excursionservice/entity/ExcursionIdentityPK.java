package org.burgas.excursionservice.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("ALL")
public final class ExcursionIdentityPK {

    private Long excursionId;
    private Long identityId;

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ExcursionIdentityPK excursionIdentityPK;

        public Builder() {
            excursionIdentityPK = new ExcursionIdentityPK();
        }

        public Builder excursionId(Long excursionId) {
            this.excursionIdentityPK.excursionId = excursionId;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.excursionIdentityPK.identityId = identityId;
            return this;
        }

        public ExcursionIdentityPK build() {
            return this.excursionIdentityPK;
        }
    }
}
