package org.burgas.flightbackend.dto;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FilialResponse extends Response {

    private Long id;
    private String name;
    private AddressResponse address;
    private String opensAt;
    private String closesAt;
    private Boolean opened;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public Boolean getOpened() {
        return opened;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilialResponse that = (FilialResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) &&
               Objects.equals(opensAt, that.opensAt) && Objects.equals(closesAt, that.closesAt) && Objects.equals(opened, that.opened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, opensAt, closesAt, opened);
    }

    @Override
    public String toString() {
        return "FilialResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address=" + address +
               ", opensAt='" + opensAt + '\'' +
               ", closesAt='" + closesAt + '\'' +
               ", opened=" + opened +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FilialResponse filialResponse;

        public Builder() {
            filialResponse = new FilialResponse();
        }

        public Builder id(Long id) {
            this.filialResponse.id = id;
            return this;
        }

        public Builder name(String name) {
            this.filialResponse.name = name;
            return this;
        }

        public Builder address(AddressResponse address) {
            this.filialResponse.address = address;
            return this;
        }

        public Builder opensAt(String opensAt) {
            this.filialResponse.opensAt = opensAt;
            return this;
        }

        public Builder closesAt(String closesAt) {
            this.filialResponse.closesAt = closesAt;
            return this;
        }

        public Builder opened(Boolean opened) {
            this.filialResponse.opened = opened;
            return this;
        }

        public FilialResponse build() {
            return this.filialResponse;
        }
    }
}
