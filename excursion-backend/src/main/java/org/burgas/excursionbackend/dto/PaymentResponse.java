package org.burgas.excursionbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class PaymentResponse extends Response {

    private Long id;
    private IdentityResponse identity;
    private ExcursionResponse excursion;
    private String payedAt;

    public Long getId() {
        return id;
    }

    public IdentityResponse getIdentity() {
        return identity;
    }

    public ExcursionResponse getExcursion() {
        return excursion;
    }

    public String getPayedAt() {
        return payedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResponse that = (PaymentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(identity, that.identity) && Objects.equals(excursion, that.excursion) &&
               Objects.equals(payedAt, that.payedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identity, excursion, payedAt);
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
               "id=" + id +
               ", identity=" + identity +
               ", excursion=" + excursion +
               ", payedAt='" + payedAt + '\'' +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final PaymentResponse paymentResponse;

        public Builder() {
            paymentResponse = new PaymentResponse();
        }

        public Builder id(Long id) {
            this.paymentResponse.id = id;
            return this;
        }

        public Builder identity(IdentityResponse identity) {
            this.paymentResponse.identity = identity;
            return this;
        }

        public Builder excursion(ExcursionResponse excursion) {
            this.paymentResponse.excursion = excursion;
            return this;
        }

        public Builder payedAt(String payedAt) {
            this.paymentResponse.payedAt = payedAt;
            return this;
        }

        public PaymentResponse build() {
            return this.paymentResponse;
        }
    }
}
