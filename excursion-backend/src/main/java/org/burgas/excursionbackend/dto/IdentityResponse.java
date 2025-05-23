package org.burgas.excursionbackend.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class IdentityResponse extends Response implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String registeredAt;
    private Boolean enabled;
    private AuthorityResponse authority;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernameNonUserDetails() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AuthorityResponse getAuthority() {
        return authority;
    }

    public void setAuthority(AuthorityResponse authority) {
        this.authority = authority;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentityResponse that = (IdentityResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) &&
               Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(registeredAt, that.registeredAt) &&
               Objects.equals(enabled, that.enabled) && Objects.equals(authority, that.authority) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, phone, registeredAt, enabled, authority, imageId);
    }

    @Override
    public String toString() {
        return "IdentityResponse{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", registeredAt='" + registeredAt + '\'' +
               ", enabled=" + enabled +
               ", authority=" + authority +
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

        public Builder authority(AuthorityResponse authority) {
            this.identityResponse.authority = authority;
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
