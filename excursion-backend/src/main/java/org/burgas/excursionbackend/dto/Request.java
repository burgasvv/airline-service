package org.burgas.excursionbackend.dto;

public abstract sealed class Request
        permits AuthorityRequest, CityRequest, CountryRequest, ExcursionRequest,
                GuideRequest, IdentityRequest, PaymentRequest, SightRequest {
}
