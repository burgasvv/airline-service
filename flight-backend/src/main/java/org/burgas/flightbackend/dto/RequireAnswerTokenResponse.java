package org.burgas.flightbackend.dto;

import java.util.UUID;

@SuppressWarnings("unused")
public final class RequireAnswerTokenResponse {

    private Long id;
    private UUID value;
    private RequireAnswerResponse requireAnswer;

    public Long getId() {
        return id;
    }

    public UUID getValue() {
        return value;
    }

    public RequireAnswerResponse getRequireAnswer() {
        return requireAnswer;
    }

    @Override
    public String toString() {
        return "RequireAnswerTokenResponse{" +
               "id=" + id +
               ", value=" + value +
               ", requireAnswer=" + requireAnswer +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RequireAnswerTokenResponse requireAnswerTokenResponse;

        public Builder() {
            requireAnswerTokenResponse = new RequireAnswerTokenResponse();
        }

        public Builder id(Long id) {
            this.requireAnswerTokenResponse.id = id;
            return this;
        }

        public Builder value(UUID value) {
            this.requireAnswerTokenResponse.value = value;
            return this;
        }

        public Builder requireAnswer(RequireAnswerResponse requireAnswer) {
            this.requireAnswerTokenResponse.requireAnswer = requireAnswer;
            return this;
        }

        public RequireAnswerTokenResponse build() {
            return this.requireAnswerTokenResponse;
        }
    }
}
