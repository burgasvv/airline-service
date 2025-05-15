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
public final class Employee extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private Long identityId;
    private Long filialId;
    private Long positionId;
    private Long imageId;

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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public Long getFilialId() {
        return filialId;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(surname, employee.surname) &&
               Objects.equals(patronymic, employee.patronymic) && Objects.equals(about, employee.about) &&
               Objects.equals(passport, employee.passport) && Objects.equals(identityId, employee.identityId) &&
               Objects.equals(filialId, employee.filialId) && Objects.equals(positionId, employee.positionId) && Objects.equals(imageId, employee.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, about, passport, identityId, filialId, positionId, imageId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Employee employee;

        public Builder() {
            employee = new Employee();
        }

        public Builder id(Long id) {
            this.employee.id = id;
            return this;
        }

        public Builder name(String name) {
            this.employee.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.employee.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.employee.patronymic = patronymic;
            return this;
        }

        public Builder about(String about) {
            this.employee.about = about;
            return this;
        }

        public Builder passport(String passport) {
            Matcher matcher = this.employee.validatePassport(passport);
            if (matcher.matches()) {
                passport = passport.replaceAll("\\s", "");
                String[] arr = passport.split("");
                passport = arr[0]+arr[1]+arr[2]+arr[3]+" "+arr[4]+arr[5]+arr[6]+arr[7]+arr[8]+arr[9];
                this.employee.passport = passport;
                return this;

            } else {
                throw new PassportNotMatchesException(PASSPORT_NOT_MATCHES.getMessage());
            }
        }

        public Builder identityId(Long identityId) {
            this.employee.identityId = identityId;
            return this;
        }

        public Builder filialId(Long filialId) {
            this.employee.filialId = filialId;
            return this;
        }

        public Builder positionId(Long positionId) {
            this.employee.positionId = positionId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.employee.imageId = imageId;
            return this;
        }

        public Employee build() {
            return this.employee;
        }
    }
}
