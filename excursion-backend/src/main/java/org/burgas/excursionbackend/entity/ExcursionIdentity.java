package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(value = ExcursionIdentityPK.class)
@SuppressWarnings("ALL")
public final class ExcursionIdentity {

    @Id
    private Long excursionId;

    @Id
    private Long identityId;

    public Long getExcursionId() {
        return excursionId;
    }

    public Long getIdentityId() {
        return identityId;
    }

    @Override
    public String toString() {
        return "ExcursionIdentity{" +
               "excursionId=" + excursionId +
               ", identityId=" + identityId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ExcursionIdentity excursionIdentity;

        public Builder() {
            excursionIdentity = new ExcursionIdentity();
        }

        public Builder excursionId(Long excursionId) {
            this.excursionIdentity.excursionId = excursionId;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.excursionIdentity.identityId = identityId;
            return this;
        }

        public ExcursionIdentity build() {
            return this.excursionIdentity;
        }
    }
}
