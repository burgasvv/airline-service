package org.burgas.flightbackend.dto;

public abstract sealed class Response
        permits AddressResponse, AirportResponse, AuthorityResponse, CityResponse, CountryResponse, DepartmentResponse,
                EmployeeResponse, FilialDepartmentResponse, FilialResponse, FlightResponse, FlightSeatResponse, IdentityResponse,
                OrderedTicketResponse, PlaneResponse, PositionResponse, RequireAnswerResponse, RequireAnswerTokenResponse,
                RequireResponse, TicketResponse {
}
