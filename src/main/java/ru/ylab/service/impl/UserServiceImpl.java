package ru.ylab.service.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.UserService;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

import java.math.BigDecimal;
import java.util.List;

/**
 * Бизнес логика работы с пользователями
 */
public class UserServiceImpl implements UserService {
    private static UserService instance;
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    private UserServiceImpl() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public UserOutDto add(UserIncomingDto dto) throws NotFoundException {
        User user = userMapper.userIncomingDtoToUser(dto);
        user.setHashPassword(passwordEncoder.encode(dto.getPassword()));
        Wallet wallet = new Wallet(
                null,
                "wallet-1",
                BigDecimal.ZERO
        );
        wallet = walletRepository.save(wallet);
        user.setWallet(wallet);
        user = userRepository.save(user);
        return userMapper.userToUserOutDto(user);
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
