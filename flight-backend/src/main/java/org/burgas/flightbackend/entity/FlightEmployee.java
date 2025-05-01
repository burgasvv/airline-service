package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Objects;

@Entity
@IdClass(value = FlightEmployeePK.class)
@SuppressWarnings("ALL")
public final class FlightEmployee {

    @Id
    private Long flightId;

    @Id
    private Long employeeId;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightEmployee that = (FlightEmployee) o;
        return Objects.equals(flightId, that.flightId) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, employeeId);
    }

    @Override
    public String toString() {
        return "FlightEmployee{" +
               "flightId=" + flightId +
               ", employeeId=" + employeeId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FlightEmployee flightEmployee;

        public Builder() {
            flightEmployee = new FlightEmployee();
        }

        public Builder flightId(Long flightId) {
            this.flightEmployee.flightId = flightId;
            return this;
        }

        public Builder employeeId(Long employeeId) {
            this.flightEmployee.employeeId = employeeId;
            return this;
        }

        public FlightEmployee build() {
            return this.flightEmployee;
        }
    }
}
