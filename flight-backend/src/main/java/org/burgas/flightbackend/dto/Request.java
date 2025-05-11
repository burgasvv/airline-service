package org.burgas.flightbackend.dto;

public abstract sealed class Request
        permits AddressRequest, AirportRequest, AuthorityRequest, DepartmentRequest, EmployeeRequest, FilialDepartmentRequest,
                FilialRequest, FlightRequest, IdentityRequest, OrderedTicketRequest, PlaneRequest,
                PositionRequest, RequireAnswerRequest, RequireRequest, TicketRequest {
}
