package ru.ylab.mapper.impl;

import ru.ylab.mapper.UserDtoMapper;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

public class UserDtoMapperImpl implements UserDtoMapper {
    private static UserDtoMapper instance;
    private final PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();

    private UserDtoMapperImpl() {
    }


    public static synchronized UserDtoMapper getInstance() {
        if (instance == null) {
            instance = new UserDtoMapperImpl();
        }
        return instance;
    }


    @Override
    public User map(UserIncomingDto dto) {
        return new User(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                passwordEncoder.encode(dto.getPassword()),
                null
        );
    }

}
