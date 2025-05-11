package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class RequireAnswerToken extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private UUID value;
    private Long requireAnswerId;

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

    public Long getRequireAnswerId() {
        return requireAnswerId;
    }

    public void setRequireAnswerId(Long requireAnswerId) {
        this.requireAnswerId = requireAnswerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequireAnswerToken that = (RequireAnswerToken) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(requireAnswerId, that.requireAnswerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, requireAnswerId);
    }

    @Override
    public String toString() {
        return "RequireAnswerToken{" +
               "id=" + id +
               ", value=" + value +
               ", requireAnswerId=" + requireAnswerId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RequireAnswerToken requireAnswerToken;

        public Builder() {
            requireAnswerToken = new RequireAnswerToken();
        }

        public Builder id(Long id) {
            this.requireAnswerToken.id = id;
            return this;
        }

        public Builder value(UUID value) {
            this.requireAnswerToken.value = value;
            return this;
        }

        public Builder requireAnswerId(Long requireAnswerId) {
            this.requireAnswerToken.requireAnswerId = requireAnswerId;
            return this;
        }

        public RequireAnswerToken build() {
            return this.requireAnswerToken;
        }
    }
}
