package ru.ylab.mapper;

import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserUpdateDto;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);

    User map(UserUpdateDto dto, User user);
}
