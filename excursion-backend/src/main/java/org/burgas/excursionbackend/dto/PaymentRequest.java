package org.burgas.excursionbackend.dto;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class PaymentRequest extends Request {

    private Long id;
    private Long identityId;
    private Long excursionId;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(identityId, that.identityId) && Objects.equals(excursionId, that.excursionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityId, excursionId);
    }
}
