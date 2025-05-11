package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RequireAnswerRequest extends Request {

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
        RequireAnswerRequest that = (RequireAnswerRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(allowed, that.allowed) && Objects.equals(explanation, that.explanation) &&
               Objects.equals(requireId, that.requireId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allowed, explanation, requireId);
    }

    @Override
    public String toString() {
        return "RequireAnswerRequest{" +
               "id=" + id +
               ", allowed=" + allowed +
               ", explanation='" + explanation + '\'' +
               ", requireId=" + requireId +
               '}';
    }
}
