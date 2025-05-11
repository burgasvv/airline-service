package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class IdentityRequest extends Request {

    private Long id;
    private Long authorityId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentityRequest that = (IdentityRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(authorityId, that.authorityId) && Objects.equals(username, that.username) &&
               Objects.equals(password, that.password) && Objects.equals(email, that.email) &&
               Objects.equals(phone, that.phone) && Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorityId, username, password, email, phone, enabled);
    }

    @Override
    public String toString() {
        return "IdentityRequest{" +
               "id=" + id +
               ", authorityId=" + authorityId +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", enabled=" + enabled +
               '}';
    }
}
