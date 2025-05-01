package org.burgas.excursionbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("ALL")
public final class ExcursionRequest {

    private Long id;
    private String name;
    private String description;
    private Long guideId;
    private Long cost;
    private LocalDateTime starts;
    private LocalDateTime ends;
    private Boolean inProgress;
    private Boolean passed;
    private List<Long> sights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public LocalDateTime getStarts() {
        return starts;
    }

    public void setStarts(LocalDateTime starts) {
        this.starts = starts;
    }

    public LocalDateTime getEnds() {
        return ends;
    }

    public void setEnds(LocalDateTime ends) {
        this.ends = ends;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public List<Long> getSights() {
        return sights;
    }

    public void setSights(List<Long> sights) {
        this.sights = sights;
    }
}
