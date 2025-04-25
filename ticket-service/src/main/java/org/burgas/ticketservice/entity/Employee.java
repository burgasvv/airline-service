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
public final class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String about;
    private String passport;
    private Long identityId;
    private Long addressId;
    private Long positionId;
    private Long filialDepartmentId;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getFilialDepartmentId() {
        return filialDepartmentId;
    }

    public void setFilialDepartmentId(Long filialDepartmentId) {
        this.filialDepartmentId = filialDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(surname, employee.surname) &&
               Objects.equals(patronymic, employee.patronymic) && Objects.equals(about, employee.about) &&
               Objects.equals(passport, employee.passport) && Objects.equals(identityId, employee.identityId) &&
               Objects.equals(addressId, employee.addressId) && Objects.equals(positionId, employee.positionId) &&
               Objects.equals(filialDepartmentId, employee.filialDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, about, passport, identityId, addressId, positionId, filialDepartmentId);
    }

    @Override
    public String toString() {
        return "Employee{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", about='" + about + '\'' +
               ", passport='" + passport + '\'' +
               ", identityId=" + identityId +
               ", addressId=" + addressId +
               ", positionId=" + positionId +
               ", filialDepartmentId=" + filialDepartmentId +
               '}';
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

        public Builder addressId(Long addressId) {
            this.employee.addressId = addressId;
            return this;
        }

        public Builder positionId(Long positionId) {
            this.employee.positionId = positionId;
            return this;
        }

        public Builder filialDepartmentId(Long filialDepartmentId) {
            this.employee.filialDepartmentId = filialDepartmentId;
            return this;
        }

        public Employee build() {
            return this.employee;
        }
    }
}
