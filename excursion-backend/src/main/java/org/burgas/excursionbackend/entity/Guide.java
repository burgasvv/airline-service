package org.burgas.excursionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.burgas.excursionbackend.exception.PhoneNotMatchesException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.burgas.excursionbackend.message.IdentityMessages.PHONE_NOT_MATCHES;

@Entity
@SuppressWarnings("unused")
public final class Guide {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String about;
    private Long imageId;

    public Matcher validatePhone(String phone) {
        Pattern compile = Pattern.compile("^\\d{10}$");
        return compile.matcher(phone);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : phone.toCharArray()) {
            if (Character.isDigit(c))
                stringBuilder.append(c);
        }
        phone = stringBuilder.toString();
        Matcher matcher = this.validatePhone(phone);
        if (matcher.matches()) {
            String[] arr = phone.split("");
            phone = "(" + arr[0]+arr[1]+arr[2] + ")-" + arr[3]+arr[4]+arr[5] + "-" + arr[6]+arr[7]+arr[8]+arr[9];
            this.phone = phone;
        } else {
            throw new PhoneNotMatchesException(PHONE_NOT_MATCHES.getMessage());
        }
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Guide guide = (Guide) o;
        return Objects.equals(id, guide.id) && Objects.equals(name, guide.name) &&
               Objects.equals(surname, guide.surname) && Objects.equals(patronymic, guide.patronymic) &&
               Objects.equals(phone, guide.phone) && Objects.equals(about, guide.about) && Objects.equals(imageId, guide.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, phone, about, imageId);
    }

    @Override
    public String toString() {
        return "Guide{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", phone='" + phone + '\'' +
               ", about='" + about + '\'' +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Guide guide;

        public Builder() {
            guide = new Guide();
        }

        public Builder id(Long id) {
            this.guide.id = id;
            return this;
        }

        public Builder name(String name) {
            this.guide.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.guide.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.guide.patronymic = patronymic;
            return this;
        }

        public Builder phone(String phone) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Character c : phone.toCharArray()) {
                if (Character.isDigit(c))
                    stringBuilder.append(c);
            }
            phone = stringBuilder.toString();
            Matcher matcher = this.guide.validatePhone(phone);
            if (matcher.matches()) {
                String[] arr = phone.split("");
                phone = "(" + arr[0]+arr[1]+arr[2] + ")-" + arr[3]+arr[4]+arr[5] + "-" + arr[6]+arr[7]+arr[8]+arr[9];
                this.guide.phone = phone;
                return this;

            } else {
                throw new PhoneNotMatchesException(PHONE_NOT_MATCHES.getMessage());
            }
        }

        public Builder about(String about) {
            this.guide.about = about;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.guide.imageId = imageId;
            return this;
        }

        public Guide build() {
            return this.guide;
        }
    }
}
