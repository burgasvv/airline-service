package org.burgas.hotelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(value = FilialDepartmentPK.class)
@SuppressWarnings("ALL")
public final class FilialDepartment extends AbstractEntity implements Serializable {

    @Id
    private Long filialId;

    @Id
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
        FilialDepartment that = (FilialDepartment) o;
        return Objects.equals(filialId, that.filialId) && Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filialId, departmentId);
    }

    @Override
    public String toString() {
        return "FilialDepartment{" +
               "filialId=" + filialId +
               ", departmentId=" + departmentId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FilialDepartment filialDepartment;

        public Builder() {
            filialDepartment = new FilialDepartment();
        }

        public Builder filialId(Long filialId) {
            this.filialDepartment.filialId = filialId;
            return this;
        }

        public Builder departmentId(Long departmentId) {
            this.filialDepartment.departmentId = departmentId;
            return this;
        }

        public FilialDepartment build() {
            return this.filialDepartment;
        }
    }
}
