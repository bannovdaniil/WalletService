package ru.ylab.mapper.impl;

import ru.ylab.mapper.UserDtoMapper;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserUpdateDto;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

public class UserDtoMapperImpl implements UserDtoMapper {
    private static UserDtoMapper instance;
    private PasswordEncoder passwordEncoder;

    public UserDtoMapperImpl() {
        passwordEncoder = PasswordEncoderSha256Impl.getInstance();
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

    @Override
    public User map(UserUpdateDto dto, User user) {
        return new User(
                dto.getId(),
                dto.getFirstName() == null ? user.getFirstName() : dto.getFirstName(),
                dto.getLastName() == null ? user.getLastName() : dto.getLastName(),
                dto.getPassword() == null ? user.getHashPassword() : passwordEncoder.encode(dto.getPassword()),
                dto.getWallet() == null ? user.getWallet() : dto.getWallet()
        );
    }
}
