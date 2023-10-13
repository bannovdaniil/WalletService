package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.mapper.UserDtoMapper;
import ru.ylab.mapper.impl.UserDtoMapperImpl;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.UserService;

import java.math.BigDecimal;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static UserService instance;
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();
    private final UserDtoMapper userDtoMapper = UserDtoMapperImpl.getInstance();

    private UserServiceImpl() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User add(UserIncomingDto dto) throws NotFoundException {
        User user = userDtoMapper.map(dto);
        user = userRepository.save(user);
        Wallet wallet = new Wallet(
                null,
                user,
                "wallet-1",
                BigDecimal.ZERO
        );
        walletRepository.save(wallet);
        user.setWallet(wallet);
        userRepository.update(user);
        return user;
    }

    @Override
    public User findById(Long userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
