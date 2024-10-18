package com.nisum.users.mapper;

import com.nisum.users.domain.Phone;
import com.nisum.users.model.PhoneRq;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    // Mapper PhoneRq to Phone
    Phone toEntity(PhoneRq phoneRq);

    // Mapper Phone to PhoneRq
    PhoneRq toDto(Phone phone);

    // Mapper PhoneRq to Phone existing
    void updateEntityFromDto(PhoneRq phoneRq, @MappingTarget Phone phone);

}
