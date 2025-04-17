package org.burgas.excursionservice.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("ALL")
public final class GuideLanguagePK {

    private Long guideId;
    private Long languageId;

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final GuideLanguagePK guideLanguagePK;

        public Builder() {
            guideLanguagePK = new GuideLanguagePK();
        }

        public Builder guideId(Long guideId) {
            this.guideLanguagePK.guideId = guideId;
            return this;
        }

        public Builder languageId(Long languageId) {
            this.guideLanguagePK.languageId = languageId;
            return this;
        }

        public GuideLanguagePK build() {
            return this.guideLanguagePK;
        }
    }
}
