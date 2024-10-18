package com.nisum.users.mapper;

import com.nisum.users.domain.User;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.model.LoginRq;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRq userRq);
    User toEntity(UserDto userDto);
    UserRs toDto(User user);
    UserDto toDto(UserRq userRq);
    UserDto toDtoUser(User user);
    UserDto toDto(LoginRq loginRq);

    default OffsetDateTime map(LocalDateTime value) {
        return value != null ? value.atOffset(ZoneOffset.UTC) : null; // O utiliza la zona horaria que necesites
    }

    default LocalDateTime map(OffsetDateTime value) {
        return value != null ? value.toLocalDateTime() : null;
    }

    void updateEntityFromDto(UserRq userRq, @MappingTarget User user);

}
