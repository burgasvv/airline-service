package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Flight {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String number;
    private Long departureId;
    private Long arrivalId;
    private Long planeId;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;
    private Boolean inProgress;
    private Boolean completed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public void setDepartureId(Long departureId) {
        this.departureId = departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Long arrivalId) {
        this.arrivalId = arrivalId;
    }

    public Long getPlaneId() {
        return planeId;
    }

    public void setPlaneId(Long planeId) {
        this.planeId = planeId;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(LocalDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(LocalDateTime arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) && Objects.equals(number, flight.number) && Objects.equals(departureId, flight.departureId) &&
               Objects.equals(arrivalId, flight.arrivalId) && Objects.equals(planeId, flight.planeId) &&
               Objects.equals(departureAt, flight.departureAt) && Objects.equals(arrivalAt, flight.arrivalAt) &&
               Objects.equals(inProgress, flight.inProgress) && Objects.equals(completed, flight.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, departureId, arrivalId, planeId, departureAt, arrivalAt, inProgress, completed);
    }

    @Override
    public String toString() {
        return "Flight{" +
               "id=" + id +
               ", number='" + number + '\'' +
               ", departureId=" + departureId +
               ", arrivalId=" + arrivalId +
               ", planeId=" + planeId +
               ", departureAt=" + departureAt +
               ", arrivalAt=" + arrivalAt +
               ", inProgress=" + inProgress +
               ", completed=" + completed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Flight flight;

        public Builder() {
            flight = new Flight();
        }

        public Builder id(Long id) {
            this.flight.id = id;
            return this;
        }

        public Builder number(String number) {
            this.flight.number = number;
            return this;
        }

        public Builder departureId(Long departureId) {
            this.flight.departureId = departureId;
            return this;
        }

        public Builder arrivalId(Long arrivalId) {
            this.flight.arrivalId = arrivalId;
            return this;
        }

        public Builder planeId(Long planeId) {
            this.flight.planeId = planeId;
            return this;
        }

        public Builder departureAt(LocalDateTime departureAt) {
            this.flight.departureAt = departureAt;
            return this;
        }

        public Builder arrivalAt(LocalDateTime arrivalAt) {
            this.flight.arrivalAt = arrivalAt;
            return this;
        }

        public Builder inProgress(Boolean inProgress) {
            this.flight.inProgress = inProgress;
            return this;
        }

        public Builder completed(Boolean completed) {
            this.flight.completed = completed;
            return this;
        }

        public Flight build() {
            return this.flight;
        }
    }
}
