package ru.ylab.mapper;

import org.mapstruct.Mapper;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;

@Mapper
public interface UserMapper {
    User userIncomingDtoToUser(UserIncomingDto dto);

    UserOutDto userToUserOutDto(User user);
}
