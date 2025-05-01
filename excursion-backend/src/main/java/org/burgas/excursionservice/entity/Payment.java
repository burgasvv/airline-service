package org.burgas.excursionservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("ALL")
public final class Payment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long identityId;
    private Long excursionId;
    private LocalDateTime payedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public LocalDateTime getPayedAt() {
        return payedAt;
    }

    public void setPayedAt(LocalDateTime payedAt) {
        this.payedAt = payedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(identityId, payment.identityId) &&
               Objects.equals(excursionId, payment.excursionId) && Objects.equals(payedAt, payment.payedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityId, excursionId, payedAt);
    }

    @Override
    public String toString() {
        return "Payment{" +
               "id=" + id +
               ", identityId=" + identityId +
               ", excursionId=" + excursionId +
               ", payedAt=" + payedAt +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Payment payment;

        public Builder() {
            payment = new Payment();
        }

        public Builder id(Long id) {
            this.payment.id = id;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.payment.identityId = identityId;
            return this;
        }

        public Builder excursionId(Long excursionId) {
            this.payment.excursionId = excursionId;
            return this;
        }

        public Builder payedAt(LocalDateTime payedAt) {
            this.payment.payedAt = payedAt;
            return this;
        }

        public Payment build() {
            return this.payment;
        }
    }
}
