package org.burgas.flightbackend.entity;

public abstract sealed class AbstractEntity
        permits Address, Airport, Authority, CabinType, City, Country, Department, Employee,
                Filial, FilialDepartment, Flight, FlightEmployee, FlightSeat, Identity, Image,
                OrderedTicket, Plane, Position, Require, RequireAnswer, RequireAnswerToken, RestoreToken, Ticket {
}
