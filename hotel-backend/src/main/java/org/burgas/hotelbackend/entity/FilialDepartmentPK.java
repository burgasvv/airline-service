package org.burgas.hotelbackend.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("ALL")
public final class FilialDepartmentPK {

    private Long filialId;
    private Long departmentId;

    public Long getFilialId() {
        return filialId;
    }

    public void setFilialId(Long filialId) {
        this.filialId = filialId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialDepartmentPK that = (FilialDepartmentPK) o;
        return Objects.equals(filialId, that.filialId) && Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filialId, departmentId);
    }

    @Override
    public String toString() {
        return "FilialDepartmentPK{" +
               "filialId=" + filialId +
               ", departmentId=" + departmentId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FilialDepartmentPK filialDepartmentPK;

        public Builder() {
            filialDepartmentPK = new FilialDepartmentPK();
        }

        public Builder filialId(Long filialId) {
            this.filialDepartmentPK.filialId = filialId;
            return this;
        }

        public Builder departmentId(Long departmentId) {
            this.filialDepartmentPK.departmentId = departmentId;
            return this;
        }

        public FilialDepartmentPK build() {
            return this.filialDepartmentPK;
        }
    }
}
