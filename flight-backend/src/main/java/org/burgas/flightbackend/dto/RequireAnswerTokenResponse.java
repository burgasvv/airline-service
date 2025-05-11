package org.burgas.flightbackend.dto;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public final class RequireAnswerTokenResponse extends Response {

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequireAnswerTokenResponse that = (RequireAnswerTokenResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(requireAnswer, that.requireAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, requireAnswer);
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
