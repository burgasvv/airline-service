package org.burgas.ticketservice.entity;

import org.burgas.ticketservice.exception.PhoneNotMatchesException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.burgas.ticketservice.message.IdentityMessage.PHONE_NOT_MATCHES;

@SuppressWarnings("unused")
public final class Identity implements Persistable<Long> {

    @Id
    private Long id;
    private Long authorityId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime registeredAt;
    private Boolean enabled;
    private Long imageId;

    @Transient
    private Boolean isNew;

    public Matcher validatePhone(String phone) {
        Pattern compile = Pattern.compile("^\\d{10}$");
        return compile.matcher(phone);
    }

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
        Matcher matcher = this.validatePhone(phone);
        if (matcher.matches()) {
            String[] arr = phone.split("");
            phone = "(" + arr[0]+arr[1]+arr[2] + ")-" + arr[3]+arr[4]+arr[5] + "-" + arr[6]+arr[7]+arr[8]+arr[9];
            this.phone = phone;
        } else {
            throw new PhoneNotMatchesException(PHONE_NOT_MATCHES.getMessage());
        }
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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(id, identity.id) && Objects.equals(authorityId, identity.authorityId) &&
               Objects.equals(username, identity.username) && Objects.equals(password, identity.password) &&
               Objects.equals(email, identity.email) && Objects.equals(phone, identity.phone) &&
               Objects.equals(registeredAt, identity.registeredAt) && Objects.equals(enabled, identity.enabled) &&
               Objects.equals(imageId, identity.imageId) && Objects.equals(isNew, identity.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorityId, username, password, email, phone, registeredAt, enabled, imageId, isNew);
    }

    @Override
    public String toString() {
        return "Identity{" +
               "id=" + id +
               ", authorityId=" + authorityId +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", registeredAt=" + registeredAt +
               ", enabled=" + enabled +
               ", imageId=" + imageId +
               ", isNew=" + isNew +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Identity identity;

        public Builder() {
            identity = new Identity();
        }

        public Builder id(Long id) {
            this.identity.id = id;
            return this;
        }

        public Builder authorityId(Long authorityId) {
            this.identity.authorityId = authorityId;
            return this;
        }

        public Builder username(String username) {
            this.identity.username = username;
            return this;
        }

        public Builder password(String password) {
            this.identity.password = password;
            return this;
        }

        public Builder email(String email) {
            this.identity.email = email;
            return this;
        }

        public Builder phone(String phone) {
            Matcher matcher = this.identity.validatePhone(phone);
            if (matcher.matches()) {
                String[] arr = phone.split("");
                phone = "(" + arr[0]+arr[1]+arr[2] + ")-" + arr[3]+arr[4]+arr[5] + "-" + arr[6]+arr[7]+arr[8]+arr[9];
                this.identity.phone = phone;
                return this;
            } else {
                throw new PhoneNotMatchesException(PHONE_NOT_MATCHES.getMessage());
            }
        }

        public Builder registeredAt(LocalDateTime registeredAt) {
            this.identity.registeredAt = registeredAt;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.identity.enabled = enabled;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.identity.imageId = imageId;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.identity.isNew = isNew;
            return this;
        }

        public Identity build() {
            return this.identity;
        }
    }
}
