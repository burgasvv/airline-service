package org.burgas.ticketservice.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class FlightResponse {

    private Long id;
    private String number;
    private AirportResponse departure;
    private AirportResponse arrival;
    private PlaneResponse plane;
    private String departureAt;
    private String arrivalAt;
    private Boolean inProgress;
    private Boolean completed;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public AirportResponse getDeparture() {
        return departure;
    }

    public AirportResponse getArrival() {
        return arrival;
    }

    public PlaneResponse getPlane() {
        return plane;
    }

    public String getDepartureAt() {
        return departureAt;
    }

    public String getArrivalAt() {
        return arrivalAt;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public Boolean getCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightResponse that = (FlightResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(departure, that.departure) &&
               Objects.equals(arrival, that.arrival) && Objects.equals(plane, that.plane) && Objects.equals(departureAt, that.departureAt) &&
               Objects.equals(arrivalAt, that.arrivalAt) && Objects.equals(inProgress, that.inProgress) && Objects.equals(completed, that.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, departure, arrival, plane, departureAt, arrivalAt, inProgress, completed);
    }

    @Override
    public String toString() {
        return "FlightResponse{" +
               "id=" + id +
               ", number='" + number + '\'' +
               ", departure=" + departure +
               ", arrival=" + arrival +
               ", plane=" + plane +
               ", departureAt='" + departureAt + '\'' +
               ", arrivalAt='" + arrivalAt + '\'' +
               ", inProgress=" + inProgress +
               ", completed=" + completed +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FlightResponse flightResponse;

        public Builder() {
            flightResponse = new FlightResponse();
        }

        public Builder id(Long id) {
            this.flightResponse.id = id;
            return this;
        }

        public Builder number(String number) {
            this.flightResponse.number = number;
            return this;
        }

        public Builder departure(AirportResponse departure) {
            this.flightResponse.departure = departure;
            return this;
        }

        public Builder arrival(AirportResponse arrival) {
            this.flightResponse.arrival = arrival;
            return this;
        }

        public Builder plane(PlaneResponse plane) {
            this.flightResponse.plane = plane;
            return this;
        }

        public Builder departureAt(String departureAt) {
            this.flightResponse.departureAt = departureAt;
            return this;
        }

        public Builder arrivalAt(String arrivalAt) {
            this.flightResponse.arrivalAt = arrivalAt;
            return this;
        }

        public Builder inProgress(Boolean inProgress) {
            this.flightResponse.inProgress = inProgress;
            return this;
        }

        public Builder completed(Boolean completed) {
            this.flightResponse.completed = completed;
            return this;
        }

        public FlightResponse build() {
            return this.flightResponse;
        }
    }
}
