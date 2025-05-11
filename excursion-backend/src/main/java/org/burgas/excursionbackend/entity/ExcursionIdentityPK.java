package org.burgas.excursionbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcursionIdentityPK that = (ExcursionIdentityPK) o;
        return Objects.equals(excursionId, that.excursionId) && Objects.equals(identityId, that.identityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excursionId, identityId);
    }

    @Override
    public String toString() {
        return "ExcursionIdentityPK{" +
               "excursionId=" + excursionId +
               ", identityId=" + identityId +
               '}';
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
