package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ylab.exception.NotFoundException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.service.UserService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Бизнес логика работы с пользователями
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserOutDto add(UserIncomingDto dto) throws NotFoundException {
        User user = userMapper.dtoToUser(dto);
        user.setHashPassword(passwordEncoder.encode(dto.getPassword()));
        Wallet wallet = new Wallet(
                null,
                "wallet-1",
                BigDecimal.ZERO
        );
        wallet = walletRepository.save(wallet);
        user.setWallet(wallet);
        user = userRepository.save(user);
        return userMapper.userToDto(user);
    }

    @Override
    public UserOutDto findById(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        return userMapper.userToDto(user);
    }

    @Override
    public List<UserOutDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userMapper.userToDto(userList);
    }
}
