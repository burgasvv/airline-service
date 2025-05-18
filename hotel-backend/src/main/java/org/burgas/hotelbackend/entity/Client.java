package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.burgas.hotelbackend.exception.PassportNotMatchesException;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.burgas.hotelbackend.message.EmployeeMessages.PASSPORT_NOT_MATCHES;

@Entity
@SuppressWarnings("ALL")
public final class Client extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private Long identityId;

    public Matcher validatePassport(String passport) {
        passport = passport.replaceAll("\\s", "");
        Pattern pattern = Pattern.compile("^\\d{10}$");
        return pattern.matcher(passport);
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

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        Matcher matcher = this.validatePassport(passport);
        if (matcher.matches()) {
            passport = passport.replaceAll("\\s", "");
            String[] arr = passport.split("");
            passport = arr[0]+arr[1]+arr[2]+arr[3]+" "+arr[4]+arr[5]+arr[6]+arr[7]+arr[8]+arr[9];
            this.passport = passport;
        } else {
            throw new PassportNotMatchesException(PASSPORT_NOT_MATCHES.getMessage());
        }
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name) && Objects.equals(surname, client.surname) &&
               Objects.equals(patronymic, client.patronymic) && Objects.equals(passport, client.passport) && Objects.equals(identityId, client.identityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, passport, identityId);
    }

    @Override
    public String toString() {
        return "Client{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", passport='" + passport + '\'' +
               ", identityId=" + identityId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Client client;

        public Builder() {
            client = new Client();
        }

        public Builder id(Long id) {
            this.client.id = id;
            return this;
        }

        public Builder name(String name) {
            this.client.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.client.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.client.patronymic = patronymic;
            return this;
        }

        public Builder passport(String passport) {
            Matcher matcher = this.client.validatePassport(passport);
            if (matcher.matches()) {
                passport = passport.replaceAll("\\s", "");
                String[] arr = passport.split("");
                passport = arr[0]+arr[1]+arr[2]+arr[3]+" "+arr[4]+arr[5]+arr[6]+arr[7]+arr[8]+arr[9];
                this.client.passport = passport;
                return this;

            } else {
                throw new PassportNotMatchesException(PASSPORT_NOT_MATCHES.getMessage());
            }
        }

        public Builder identityId(Long identityId) {
            this.client.identityId = identityId;
            return this;
        }

        public Client build() {
            return this.client;
        }
    }
}
