package org.burgas.flightbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("ALL")
public class FlightEmployeePK {

    private Long flightId;
    private Long employeeId;

    public Long getFlightId() {
        return flightId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightEmployeePK that = (FlightEmployeePK) o;
        return Objects.equals(flightId, that.flightId) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, employeeId);
    }

    @Override
    public String toString() {
        return "FlightEmployeePK{" +
               "flightId=" + flightId +
               ", employeeId=" + employeeId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FlightEmployeePK flightEmployeePK;

        public Builder() {
            flightEmployeePK = new FlightEmployeePK();
        }

        public Builder flightId(Long flightId) {
            this.flightEmployeePK.flightId = flightId;
            return this;
        }

        public Builder employeeId(Long employeeId) {
            this.flightEmployeePK.employeeId = employeeId;
            return this;
        }

        public FlightEmployeePK build() {
            return this.flightEmployeePK;
        }
    }
}
