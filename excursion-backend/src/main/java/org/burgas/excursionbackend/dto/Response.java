package org.burgas.excursionbackend.dto;

public abstract sealed class Response
        permits AuthorityResponse, CityResponse, CountryResponse, ExcursionResponse,
                GuideResponse, IdentityResponse, PaymentResponse, SightResponse {
}
