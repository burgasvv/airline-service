package org.burgas.excursionbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GuideLanguagePK that = (GuideLanguagePK) o;
        return Objects.equals(guideId, that.guideId) && Objects.equals(languageId, that.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guideId, languageId);
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
