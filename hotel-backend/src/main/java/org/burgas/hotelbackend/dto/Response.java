package org.burgas.hotelbackend.dto;

public abstract sealed class Response
        permits AddressResponse, AuthorityResponse, CityResponse, CountryResponse, DepartmentResponse,
                FilialResponse, HotelResponse, IdentityResponse, PositionResponse {
}
