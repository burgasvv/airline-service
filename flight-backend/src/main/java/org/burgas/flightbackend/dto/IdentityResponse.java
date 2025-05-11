package org.burgas.flightbackend.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class IdentityResponse extends Response implements UserDetails {

    private Long id;
    private AuthorityResponse authority;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String registeredAt;
    private Boolean enabled;
    private Long imageId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(this.authority.getAuthority())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.getEnabled() || !UserDetails.super.isEnabled();
    }

    public Long getId() {
        return id;
    }

    public AuthorityResponse getAuthority() {
        return authority;
    }

    public String getUsernameNotDetails() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentityResponse that = (IdentityResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(authority, that.authority) && Objects.equals(username, that.username) &&
               Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) &&
               Objects.equals(registeredAt, that.registeredAt) && Objects.equals(enabled, that.enabled) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority, username, password, email, phone, registeredAt, enabled, imageId);
    }

    @Override
    public String toString() {
        return "IdentityResponse{" +
               "id=" + id +
               ", authority=" + authority +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", registeredAt='" + registeredAt + '\'' +
               ", enabled=" + enabled +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final IdentityResponse identityResponse;

        public Builder() {
            identityResponse = new IdentityResponse();
        }

        public Builder id(Long id) {
            this.identityResponse.id = id;
            return this;
        }

        public Builder authority(AuthorityResponse authority) {
            this.identityResponse.authority = authority;
            return this;
        }

        public Builder username(String username) {
            this.identityResponse.username = username;
            return this;
        }

        public Builder password(String password) {
            this.identityResponse.password = password;
            return this;
        }

        public Builder email(String email) {
            this.identityResponse.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.identityResponse.phone = phone;
            return this;
        }

        public Builder registeredAt(String registeredAt) {
            this.identityResponse.registeredAt = registeredAt;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.identityResponse.enabled = enabled;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.identityResponse.imageId = imageId;
            return this;
        }

        public IdentityResponse build() {
            return this.identityResponse;
        }
    }
}
