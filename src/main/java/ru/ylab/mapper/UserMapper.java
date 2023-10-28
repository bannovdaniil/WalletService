package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToUser(UserIncomingDto dto);

    List<UserOutDto> userToDto(List<User> userList);

    default UserOutDto userToDto(User user) {
        return new UserOutDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

}
