package org.burgas.excursionbackend.entity;

public abstract sealed class AbstractEntity
        permits Authority, City, Country, Excursion, ExcursionIdentity, ExcursionSight,
                Guide, GuideLanguage, Identity, Image, Language, Payment, Sight {
}
