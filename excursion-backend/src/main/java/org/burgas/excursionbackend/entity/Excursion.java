package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Excursion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long guideId;
    private Long cost;
    private LocalDateTime starts;
    private LocalDateTime ends;
    private Boolean inProgress;
    private Boolean passed;
    private Long imageId;

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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Excursion excursion = (Excursion) o;
        return Objects.equals(id, excursion.id) && Objects.equals(name, excursion.name) &&
               Objects.equals(description, excursion.description) && Objects.equals(guideId, excursion.guideId) &&
               Objects.equals(cost, excursion.cost) && Objects.equals(starts, excursion.starts) &&
               Objects.equals(ends, excursion.ends) && Objects.equals(inProgress, excursion.inProgress) &&
               Objects.equals(passed, excursion.passed) && Objects.equals(imageId, excursion.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, guideId, cost, starts, ends, inProgress, passed, imageId);
    }

    @Override
    public String toString() {
        return "Excursion{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", guideId=" + guideId +
               ", cost=" + cost +
               ", starts=" + starts +
               ", ends=" + ends +
               ", inProgress=" + inProgress +
               ", passed=" + passed +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Excursion excursion;

        public Builder() {
            excursion = new Excursion();
        }

        public Builder id(Long id) {
            this.excursion.id = id;
            return this;
        }

        public Builder name(String name) {
            this.excursion.name = name;
            return this;
        }

        public Builder description(String description) {
            this.excursion.description = description;
            return this;
        }

        public Builder guideId(Long guideId) {
            this.excursion.guideId = guideId;
            return this;
        }

        public Builder cost(Long cost) {
            this.excursion.cost = cost;
            return this;
        }

        public Builder starts(LocalDateTime starts) {
            this.excursion.starts = starts;
            return this;
        }

        public Builder ends(LocalDateTime ends) {
            this.excursion.ends = ends;
            return this;
        }

        public Builder inProgress(Boolean inProgress) {
            this.excursion.inProgress = inProgress;
            return this;
        }

        public Builder passed(Boolean passed) {
            this.excursion.passed = passed;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.excursion.imageId = imageId;
            return this;
        }

        public Excursion build() {
            return this.excursion;
        }
    }
}
