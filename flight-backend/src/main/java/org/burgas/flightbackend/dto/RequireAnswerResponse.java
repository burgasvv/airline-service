package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RequireAnswerResponse extends Response {

    private Long id;
    private Boolean allowed;
    private String explanation;
    private RequireResponse require;

    public Long getId() {
        return id;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public String getExplanation() {
        return explanation;
    }

    public RequireResponse getRequire() {
        return require;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequireAnswerResponse that = (RequireAnswerResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(allowed, that.allowed) && Objects.equals(explanation, that.explanation) &&
               Objects.equals(require, that.require);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allowed, explanation, require);
    }

    @Override
    public String toString() {
        return "RequireAnswerResponse{" +
               "id=" + id +
               ", allowed=" + allowed +
               ", explanation='" + explanation + '\'' +
               ", require=" + require +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RequireAnswerResponse requireAnswerResponse;

        public Builder() {
            requireAnswerResponse = new RequireAnswerResponse();
        }

        public Builder id(Long id) {
            this.requireAnswerResponse.id = id;
            return this;
        }

        public Builder allowed(Boolean allowed) {
            this.requireAnswerResponse.allowed = allowed;
            return this;
        }

        public Builder explanation(String explanation) {
            this.requireAnswerResponse.explanation = explanation;
            return this;
        }

        public Builder require(RequireResponse require) {
            this.requireAnswerResponse.require = require;
            return this;
        }

        public RequireAnswerResponse build() {
            return this.requireAnswerResponse;
        }
    }
}
