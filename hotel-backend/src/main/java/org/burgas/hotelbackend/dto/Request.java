package org.burgas.hotelbackend.dto;

public abstract sealed class Request
        permits AddressRequest, AuthorityRequest, CityRequest, CountryRequest, DepartmentRequest,
                FilialRequest, HotelRequest, IdentityRequest, PositionRequest {
}
