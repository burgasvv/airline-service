package org.burgas.hotelbackend.dto;

import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class IdentityRequest {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime registeredAt;
    private Boolean enabled;
    private Long authorityId;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
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
        IdentityRequest that = (IdentityRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) &&
               Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(registeredAt, that.registeredAt) &&
               Objects.equals(enabled, that.enabled) && Objects.equals(authorityId, that.authorityId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, phone, registeredAt, enabled, authorityId, imageId);
    }

    @Override
    public String toString() {
        return "IdentityRequest{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", registeredAt=" + registeredAt +
               ", enabled=" + enabled +
               ", authorityId=" + authorityId +
               ", imageId=" + imageId +
               '}';
    }
}
