package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(value = GuideLanguagePK.class)
@SuppressWarnings("ALL")
public final class GuideLanguage extends AbstractEntity implements Serializable {

    @Id
    private Long guideId;

    @Id
    private Long languageId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GuideLanguage that = (GuideLanguage) o;
        return Objects.equals(guideId, that.guideId) && Objects.equals(languageId, that.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guideId, languageId);
    }

    @Override
    public String toString() {
        return "GuideLanguage{" +
               "guideId=" + guideId +
               ", languageId=" + languageId +
               '}';
    }

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
