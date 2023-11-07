package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;

import java.util.List;

/**
 * Маппер для User
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
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
