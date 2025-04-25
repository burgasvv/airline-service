package org.burgas.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class RestoreToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private UUID value;
    private Long identityId;

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestoreToken that = (RestoreToken) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(identityId, that.identityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, identityId);
    }

    @Override
    public String toString() {
        return "RestoreToken{" +
               "id=" + id +
               ", value=" + value +
               ", identityId=" + identityId +
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

        public RestoreToken build() {
            return this.restoreToken;
        }
    }
}
