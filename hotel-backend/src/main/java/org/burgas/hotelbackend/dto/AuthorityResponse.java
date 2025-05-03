package org.burgas.hotelbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class AuthorityResponse {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityResponse that = (AuthorityResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AuthorityResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final AuthorityResponse authorityResponse;

        public Builder() {
            authorityResponse = new AuthorityResponse();
        }

        public Builder id(Long id) {
            this.authorityResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.authorityResponse.name = name;
            return this;
        }

        public AuthorityResponse build() {
            return this.authorityResponse;
        }
    }
}
