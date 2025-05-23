package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RequireResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private Boolean closed;
    private IdentityResponse admin;
    private IdentityResponse user;

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

    public String getPassport() {
        return passport;
    }

    public Boolean getClosed() {
        return closed;
    }

    public IdentityResponse getAdmin() {
        return admin;
    }

    public IdentityResponse getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequireResponse that = (RequireResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(passport, that.passport) &&
               Objects.equals(closed, that.closed) && Objects.equals(admin, that.admin) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, passport, closed, admin, user);
    }

    @Override
    public String toString() {
        return "RequireResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", passport='" + passport + '\'' +
               ", closed=" + closed +
               ", admin=" + admin +
               ", user=" + user +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final RequireResponse requireResponse;

        public Builder() {
            requireResponse = new RequireResponse();
        }

        public Builder id(Long id) {
            this.requireResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.requireResponse.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.requireResponse.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.requireResponse.patronymic = patronymic;
            return this;
        }

        public Builder passport(String passport) {
            this.requireResponse.passport = passport;
            return this;
        }

        public Builder closed(Boolean closed) {
            this.requireResponse.closed = closed;
            return this;
        }

        public Builder admin(IdentityResponse admin) {
            this.requireResponse.admin = admin;
            return this;
        }

        public Builder user(IdentityResponse user) {
            this.requireResponse.user = user;
            return this;
        }

        public RequireResponse build() {
            return this.requireResponse;
        }
    }
}
