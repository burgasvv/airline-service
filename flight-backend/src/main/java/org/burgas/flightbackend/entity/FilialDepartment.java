package org.burgas.flightbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class FilialDepartment extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long filialId;
    private Long departmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return Objects.equals(id, that.id) && Objects.equals(filialId, that.filialId) && Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filialId, departmentId);
    }

    @Override
    public String toString() {
        return "FilialDepartment{" +
               "id=" + id +
               ", filialId=" + filialId +
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

        public Builder id(Long id) {
            this.filialDepartment.id = id;
            return this;
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
