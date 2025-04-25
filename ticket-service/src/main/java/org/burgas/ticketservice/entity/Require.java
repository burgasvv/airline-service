package org.burgas.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.burgas.ticketservice.exception.PassportNotMatchesException;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.burgas.ticketservice.message.EmployeeMessage.PASSPORT_NOT_MATCHES;

@Entity
@SuppressWarnings("unused")
public final class Require {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private Boolean closed;
    private Long adminId;
    private Long userId;

    private Matcher validatePassport(String passport) {
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Require require = (Require) o;
        return Objects.equals(id, require.id) && Objects.equals(name, require.name) && Objects.equals(surname, require.surname) &&
               Objects.equals(patronymic, require.patronymic) && Objects.equals(passport, require.passport) &&
               Objects.equals(closed, require.closed) && Objects.equals(adminId, require.adminId) && Objects.equals(userId, require.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, passport, closed, adminId, userId);
    }

    @Override
    public String toString() {
        return "Require{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", passport='" + passport + '\'' +
               ", closed=" + closed +
               ", adminId=" + adminId +
               ", userId=" + userId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Require require;

        public Builder() {
            require = new Require();
        }

        public Builder id(Long id) {
            this.require.id = id;
            return this;
        }

        public Builder name(String name) {
            this.require.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.require.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.require.patronymic = patronymic;
            return this;
        }

        public Builder passport(String passport) {
            Matcher matcher = this.require.validatePassport(passport);
            if (matcher.matches()) {
                passport = passport.replaceAll("\\s", "");
                String[] arr = passport.split("");
                passport = arr[0]+arr[1]+arr[2]+arr[3]+" "+arr[4]+arr[5]+arr[6]+arr[7]+arr[8]+arr[9];
                this.require.passport = passport;
                return this;

            } else {
                throw new PassportNotMatchesException(PASSPORT_NOT_MATCHES.getMessage());
            }
        }

        public Builder closed(Boolean closed) {
            this.require.closed = closed;
            return this;
        }

        public Builder adminId(Long adminId) {
            this.require.adminId = adminId;
            return this;
        }

        public Builder userId(Long userId) {
            this.require.userId = userId;
            return this;
        }

        public Require build() {
            return this.require;
        }
    }
}