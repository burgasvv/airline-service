package org.burgas.excursionbackend.dto;

import org.burgas.excursionbackend.entity.Language;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class GuideResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String about;
    private Long imageId;
    private List<Language> languages;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public String getAbout() {
        return about;
    }

    public Long getImageId() {
        return imageId;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GuideResponse that = (GuideResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(phone, that.phone) && Objects.equals(about, that.about) &&
               Objects.equals(imageId, that.imageId) && Objects.equals(languages, that.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, phone, about, imageId, languages);
    }

    @Override
    public String toString() {
        return "GuideResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", phone='" + phone + '\'' +
               ", about='" + about + '\'' +
               ", imageId=" + imageId +
               ", languages=" + languages +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final GuideResponse guideResponse;

        public Builder() {
            guideResponse = new GuideResponse();
        }

        public Builder id(Long id) {
            this.guideResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.guideResponse.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.guideResponse.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.guideResponse.patronymic = patronymic;
            return this;
        }

        public Builder phone(String phone) {
            this.guideResponse.phone = phone;
            return this;
        }

        public Builder about(String about) {
            this.guideResponse.about = about;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.guideResponse.imageId = imageId;
            return this;
        }

        public Builder languages(List<Language> languages) {
            this.guideResponse.languages = languages;
            return this;
        }

        public GuideResponse build() {
            return this.guideResponse;
        }
    }
}
