package org.burgas.hotelbackend.mapper;

import org.burgas.hotelbackend.dto.FilialRequest;
import org.burgas.hotelbackend.dto.FilialResponse;
import org.burgas.hotelbackend.entity.Address;
import org.burgas.hotelbackend.entity.Filial;
import org.burgas.hotelbackend.entity.FilialDepartment;
import org.burgas.hotelbackend.handler.MapperDataHandler;
import org.burgas.hotelbackend.repository.AddressRepository;
import org.burgas.hotelbackend.repository.FilialDepartmentRepository;
import org.burgas.hotelbackend.repository.FilialRepository;
import org.burgas.hotelbackend.repository.HotelRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public final class FilialMapper implements MapperDataHandler<FilialRequest, Filial, FilialResponse> {

    private final FilialRepository filialRepository;
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final FilialDepartmentRepository filialDepartmentRepository;

    public FilialMapper(
            FilialRepository filialRepository, HotelRepository hotelRepository, HotelMapper hotelMapper,
            AddressRepository addressRepository, AddressMapper addressMapper, FilialDepartmentRepository filialDepartmentRepository
    ) {
        this.filialRepository = filialRepository;
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.filialDepartmentRepository = filialDepartmentRepository;
    }

    private @Nullable Address getAddress(FilialRequest filialRequest) {
        Address address = null;
        if (filialRequest.getAddress() != null && filialRequest.getAddress().getId() != null) {
            address = this.addressRepository.findById(filialRequest.getAddress().getId()).orElse(null);

        } else if (filialRequest.getAddress() != null) {
            address = this.addressRepository.save(
                    this.addressMapper.toEntity(filialRequest.getAddress())
            );
        }
        return address;
    }

    private void handleFilialDepartment(FilialRequest filialRequest, Filial filial) {
        if (filialRequest.getDepartmentIds() != null && !filialRequest.getDepartmentIds().isEmpty()) {
            this.filialDepartmentRepository.deleteFilialDepartmentsByFilialId(filial.getId());
            filialRequest.getDepartmentIds().forEach(
                    departmentId -> this.filialDepartmentRepository.save(
                            FilialDepartment.builder()
                                    .filialId(filial.getId())
                                    .departmentId(departmentId)
                                    .build()
                    )
            );
        }

        if (filialRequest.getDepartmentIds() != null && filialRequest.getDepartmentIds().isEmpty()) {
            this.filialDepartmentRepository.deleteFilialDepartmentsByFilialId(filial.getId());
        }
    }

    @Override
    public Filial toEntity(FilialRequest filialRequest) {
        Long filialId = this.getData(filialRequest.getId(), 0L);
        return this.filialRepository.findById(filialId)
                .map(
                        filial -> {
                            Address address = getAddress(filialRequest);
                            Filial saved = this.filialRepository.save(
                                    Filial.builder()
                                            .id(filial.getId())
                                            .hotelId(this.getData(filialRequest.getHotelId(), filial.getHotelId()))
                                            .addressId(this.getData(address != null ? address.getId() : null, filial.getAddressId()))
                                            .luxRooms(this.getData(filialRequest.getLuxRooms(), filial.getLuxRooms()))
                                            .economyRooms(this.getData(filialRequest.getEconomyRooms(), filial.getEconomyRooms()))
                                            .imageId(this.getData(filialRequest.getImageId(), filial.getImageId()))
                                            .build()
                            );

                            this.handleFilialDepartment(filialRequest, filial);

                            return saved;
                        }
                )
                .orElseGet(
                        () -> {
                            Address address = getAddress(filialRequest);
                            Filial filial = this.filialRepository.save(
                                    Filial.builder()
                                            .hotelId(filialRequest.getHotelId())
                                            .addressId(address != null ? address.getId() : null)
                                            .luxRooms(filialRequest.getLuxRooms())
                                            .economyRooms(filialRequest.getEconomyRooms())
                                            .imageId(filialRequest.getImageId())
                                            .build()
                            );

                            this.handleFilialDepartment(filialRequest, filial);

                            return filial;
                        }
                );
    }

    @Override
    public FilialResponse toResponse(Filial filial) {
        return FilialResponse.builder()
                .id(filial.getId())
                .hotel(
                        this.hotelRepository.findById(filial.getHotelId())
                                .map(this.hotelMapper::toResponse)
                                .orElse(null)
                )
                .address(
                        this.addressRepository.findById(filial.getAddressId())
                                .map(this.addressMapper::toResponse)
                                .orElse(null)
                )
                .luxRooms(filial.getLuxRooms())
                .economyRooms(filial.getEconomyRooms())
                .imageId(filial.getImageId())
                .build();
    }
}
