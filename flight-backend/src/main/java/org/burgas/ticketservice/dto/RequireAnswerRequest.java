package org.burgas.ticketservice.dto;

@SuppressWarnings("unused")
public final class RequireAnswerRequest {

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
}
