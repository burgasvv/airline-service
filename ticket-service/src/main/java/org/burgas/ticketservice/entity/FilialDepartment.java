package org.burgas.ticketservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FilialDepartment implements Persistable<Long> {

    @Id
    private Long id;
    private Long filialId;
    private Long departmentId;

    @Transient
    private Boolean isNew;

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
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialDepartment that = (FilialDepartment) o;
        return Objects.equals(id, that.id) && Objects.equals(filialId, that.filialId) &&
               Objects.equals(departmentId, that.departmentId) && Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filialId, departmentId, isNew);
    }

    @Override
    public String toString() {
        return "FilialDepartment{" +
               "id=" + id +
               ", filialId=" + filialId +
               ", departmentId=" + departmentId +
               ", isNew=" + isNew +
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

        public Builder isNew(Boolean isNew) {
            this.filialDepartment.isNew = isNew;
            return this;
        }

        public FilialDepartment build() {
            return this.filialDepartment;
        }
    }
}
