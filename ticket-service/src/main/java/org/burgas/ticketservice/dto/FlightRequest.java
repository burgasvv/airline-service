package org.burgas.ticketservice.dto;

import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class FlightRequest {

    private Long id;
    private Long departureId;
    private Long arrivalId;
    private Long planeId;
    private Long businessPrice;
    private Long economyPrice;
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

    public Long getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(Long businessPrice) {
        this.businessPrice = businessPrice;
    }

    public Long getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(Long economyPrice) {
        this.economyPrice = economyPrice;
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
        FlightRequest that = (FlightRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(departureId, that.departureId) &&
               Objects.equals(arrivalId, that.arrivalId) && Objects.equals(planeId, that.planeId) &&
               Objects.equals(businessPrice, that.businessPrice) && Objects.equals(economyPrice, that.economyPrice) &&
               Objects.equals(departureAt, that.departureAt) && Objects.equals(arrivalAt, that.arrivalAt) &&
               Objects.equals(inProgress, that.inProgress) && Objects.equals(completed, that.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureId, arrivalId, planeId, businessPrice, economyPrice, departureAt, arrivalAt, inProgress, completed);
    }
}
