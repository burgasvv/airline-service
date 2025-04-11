package org.burgas.ticketservice.dto;

@SuppressWarnings("unused")
public final class RequireAnswerResponse {

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
