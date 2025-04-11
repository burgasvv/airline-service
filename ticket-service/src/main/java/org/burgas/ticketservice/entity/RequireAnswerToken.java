package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public final class RequireAnswerToken implements Persistable<Long> {

    @Id
    private Long id;
    private UUID value;
    private Long requireAnswerId;

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

    public Long getRequireAnswerId() {
        return requireAnswerId;
    }

    public void setRequireAnswerId(Long requireAnswerId) {
        this.requireAnswerId = requireAnswerId;
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
        RequireAnswerToken that = (RequireAnswerToken) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) &&
               Objects.equals(requireAnswerId, that.requireAnswerId) && Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, requireAnswerId, isNew);
    }

    @Override
    public String toString() {
        return "RequireAnswerToken{" +
               "id=" + id +
               ", value=" + value +
               ", requireAnswerId=" + requireAnswerId +
               ", isNew=" + isNew +
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

        public Builder isNew(Boolean isNew) {
            this.requireAnswerToken.isNew = isNew;
            return this;
        }

        public RequireAnswerToken build() {
            return this.requireAnswerToken;
        }
    }
}
