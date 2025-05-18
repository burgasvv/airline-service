package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class ClientResponse extends Response {

    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private IdentityResponse identity;

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

    public IdentityResponse getIdentity() {
        return identity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientResponse that = (ClientResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) &&
               Objects.equals(patronymic, that.patronymic) && Objects.equals(passport, that.passport) && Objects.equals(identity, that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, passport, identity);
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", passport='" + passport + '\'' +
               ", identity=" + identity +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ClientResponse clientResponse;

        public Builder() {
            clientResponse = new ClientResponse();
        }

        public Builder id(Long id) {
            this.clientResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.clientResponse.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.clientResponse.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.clientResponse.patronymic = patronymic;
            return this;
        }

        public Builder passport(String passport) {
            this.clientResponse.passport = passport;
            return this;
        }

        public Builder identity(IdentityResponse identity) {
            this.clientResponse.identity = identity;
            return this;
        }

        public ClientResponse build() {
            return this.clientResponse;
        }
    }
}
