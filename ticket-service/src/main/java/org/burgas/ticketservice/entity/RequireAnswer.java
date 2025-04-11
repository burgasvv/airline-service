package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RequireAnswer implements Persistable<Long> {

    @Id
    private Long id;
    private Boolean allowed;
    private String explanation;
    private Long requireId;

    @Transient
    private Boolean isNew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Long getRequireId() {
        return requireId;
    }

    public void setRequireId(Long requireId) {
        this.requireId = requireId;
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
        RequireAnswer that = (RequireAnswer) o;
        return Objects.equals(id, that.id) && Objects.equals(allowed, that.allowed) &&
               Objects.equals(explanation, that.explanation) && Objects.equals(requireId, that.requireId) && Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allowed, explanation, requireId, isNew);
    }

    @Override
    public String toString() {
        return "RequireAnswer{" +
               "id=" + id +
               ", allowed=" + allowed +
               ", explanation='" + explanation + '\'' +
               ", requireId=" + requireId +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RequireAnswer requireAnswer;

        public Builder() {
            requireAnswer = new RequireAnswer();
        }

        public Builder id(Long id) {
            this.requireAnswer.id = id;
            return this;
        }

        public Builder allowed(Boolean allowed) {
            this.requireAnswer.allowed = allowed;
            return this;
        }

        public Builder explanation(String explanation) {
            this.requireAnswer.explanation = explanation;
            return this;
        }

        public Builder requireId(Long requireId) {
            this.requireAnswer.requireId = requireId;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.requireAnswer.isNew = isNew;
            return this;
        }

        public RequireAnswer build() {
            return this.requireAnswer;
        }
    }
}
