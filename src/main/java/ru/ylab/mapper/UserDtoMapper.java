package ru.ylab.mapper;

import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);
}
