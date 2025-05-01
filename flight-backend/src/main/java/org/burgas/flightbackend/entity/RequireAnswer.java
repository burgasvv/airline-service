package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class RequireAnswer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Boolean allowed;
    private String explanation;
    private Long requireId;

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequireAnswer that = (RequireAnswer) o;
        return Objects.equals(id, that.id) && Objects.equals(allowed, that.allowed) && Objects.equals(explanation, that.explanation) &&
               Objects.equals(requireId, that.requireId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allowed, explanation, requireId);
    }

    @Override
    public String toString() {
        return "RequireAnswer{" +
               "id=" + id +
               ", allowed=" + allowed +
               ", explanation='" + explanation + '\'' +
               ", requireId=" + requireId +
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

        public RequireAnswer build() {
            return this.requireAnswer;
        }
    }
}
