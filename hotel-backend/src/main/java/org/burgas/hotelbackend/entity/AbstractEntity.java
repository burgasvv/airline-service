package org.burgas.hotelbackend.entity;

public abstract sealed class AbstractEntity
        permits Address, Authority, City, Country, Department, Filial, FilialDepartment,
                Hotel, Identity, Image, Position, Status {
}
