package org.burgas.excursionservice.dto;

@SuppressWarnings("ALL")
public final class PaymentResponse {

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
