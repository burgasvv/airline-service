package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public final class RestoreToken implements Persistable<Long> {

    @Id
    private Long id;
    private UUID value;
    private Long identityId;

    @Transient
    private Boolean isNew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
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
        RestoreToken that = (RestoreToken) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) &&
               Objects.equals(identityId, that.identityId) && Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, identityId, isNew);
    }

    @Override
    public String toString() {
        return "RestoreToken{" +
               "id=" + id +
               ", value=" + value +
               ", identityId=" + identityId +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RestoreToken restoreToken;

        public Builder() {
            restoreToken = new RestoreToken();
        }

        public Builder id(Long id) {
            this.restoreToken.id = id;
            return this;
        }

        public Builder value(UUID value) {
            this.restoreToken.value = value;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.restoreToken.identityId = identityId;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.restoreToken.isNew = isNew;
            return this;
        }

        public RestoreToken build() {
            return this.restoreToken;
        }
    }
}
