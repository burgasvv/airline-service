package org.burgas.excursionbackend.dto;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("unused")
public final class AuthorityResponse implements GrantedAuthority {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return getName();
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
