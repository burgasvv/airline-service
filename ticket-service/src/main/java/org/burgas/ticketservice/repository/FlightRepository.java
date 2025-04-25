package org.burgas.ticketservice.repository;

import org.burgas.ticketservice.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from flight f
                        join airport air on air.id = f.departure_id
                        join airport air2 on air2.id = f.arrival_id
                        join address add on add.id = air.address_id
                        join address add2 on add2.id = air2.address_id
                        join city c on c.id = add.city_id
                        join city c2 on c2.id = add2.city_id
                    where c.id = :departureCityId and c2.id = :arrivalCityId and f.departure_at::date = :departureAtDate
                    """
    )
    List<Flight> findFlightsByDepartureCityAndArrivalCityAndDepartureDate(Long departureCityId, Long arrivalCityId, LocalDate departureAtDate);

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from flight f
                        join airport air on air.id = f.departure_id
                        join airport air2 on air2.id = f.arrival_id
                        join address add on add.id = air.address_id
                        join address add2 on add2.id = air2.address_id
                        join city c on c.id = add.city_id
                        join city c2 on c2.id = add2.city_id
                    where c.id = :departureCityId and c2.id = :arrivalCityId
                    """
    )
    List<Flight> findFlightsByDepartureCityIdAndArrivalCityId(Long departureCityId, Long arrivalCityId);

    @Query(
            nativeQuery = true,
            value = """
                    select f.* from flight f
                        join airport air on air.id = f.departure_id
                        join airport air2 on air2.id = f.arrival_id
                        join address add on add.id = air.address_id
                        join address add2 on add2.id = air2.address_id
                        join city c on c.id = add.city_id
                        join city c2 on c2.id = add2.city_id
                    where c.id = :departureCityId and c2.id = :arrivalCityId and f.arrival_at > :arrivalDateTime
                    """
    )
    List<Flight> findFlightsByDepartureCityIdAndArrivalCityIdBack(Long departureCityId, Long arrivalCityId, LocalDateTime arrivalDateTime);

    @Modifying
    @Query(
            nativeQuery = true,
            value = """
                    insert into flight_employee(flight_id, employee_id) values (:flightId, :employeeId)
                    """
    )
    void insertIntoFlightEmployee(Long flightId, Long employeeId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = """
                    delete from flight_employee fe where fe.flight_id = :flightId and fe.employee_id = :employeeId
                    """
    )
    void deleteFromFlightEmployee(Long flightId, Long employeeId);
}
