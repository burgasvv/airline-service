package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(value = GuideLanguagePK.class)
@SuppressWarnings("ALL")
public final class GuideLanguage {

    @Id
    private Long guideId;

    @Id
    private Long languageId;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final GuideLanguage guideLanguage;

        public Builder() {
            guideLanguage = new GuideLanguage();
        }

        public Builder guideId(Long guideId) {
            this.guideLanguage.guideId = guideId;
            return this;
        }

        public Builder languageId(Long languageId) {
            this.guideLanguage.languageId = languageId;
            return this;
        }

        public GuideLanguage build() {
            return this.guideLanguage;
        }
    }
}
