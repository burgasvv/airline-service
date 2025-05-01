package org.burgas.excursionbackend.dto;

import java.util.List;

@SuppressWarnings("ALL")
public final class ExcursionResponse {

    private Long id;
    private String name;
    private String description;
    private String guide;
    private Long cost;
    private String starts;
    private String ends;
    private Boolean inProgress;
    private Boolean passed;
    private Long imageId;
    private List<SightResponse> sights;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGuide() {
        return guide;
    }

    public Long getCost() {
        return cost;
    }

    public String getStarts() {
        return starts;
    }

    public String getEnds() {
        return ends;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public Boolean getPassed() {
        return passed;
    }

    public Long getImageId() {
        return imageId;
    }

    public List<SightResponse> getSights() {
        return sights;
    }

    @Override
    public String toString() {
        return "ExcursionResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", guide='" + guide + '\'' +
               ", cost=" + cost +
               ", starts='" + starts + '\'' +
               ", ends='" + ends + '\'' +
               ", inProgress=" + inProgress +
               ", passed=" + passed +
               ", imageId=" + imageId +
               ", sights=" + sights +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ExcursionResponse excursionResponse;

        public Builder() {
            excursionResponse = new ExcursionResponse();
        }

        public Builder id(Long id) {
            this.excursionResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.excursionResponse.name = name;
            return this;
        }

        public Builder description(String description) {
            this.excursionResponse.description = description;
            return this;
        }

        public Builder guide(String guide) {
            this.excursionResponse.guide = guide;
            return this;
        }

        public Builder cost(Long cost) {
            this.excursionResponse.cost = cost;
            return this;
        }

        public Builder starts(String starts) {
            this.excursionResponse.starts = starts;
            return this;
        }

        public Builder ends(String ends) {
            this.excursionResponse.ends = ends;
            return this;
        }

        public Builder inProgress(Boolean inProgress) {
            this.excursionResponse.inProgress = inProgress;
            return this;
        }

        public Builder passed(Boolean passed) {
            this.excursionResponse.passed = passed;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.excursionResponse.imageId = imageId;
            return this;
        }

        public Builder sights(List<SightResponse> sights) {
            this.excursionResponse.sights = sights;
            return this;
        }

        public ExcursionResponse build() {
            return this.excursionResponse;
        }
    }
}
