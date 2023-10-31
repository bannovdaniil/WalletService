package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.service.impl.UserServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private WalletRepository mockWalletRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
        Mockito.reset(mockWalletRepository);
        Mockito.reset(mockPasswordEncoder);
        Mockito.doReturn("mockHashPassword").when(mockPasswordEncoder).encode(Mockito.anyString());
    }

    @Test
    void add() throws NotFoundException {
        Long expectedId = 1L;
        UserIncomingDto dto = new UserIncomingDto("f1 name", "l1 name", "password");

        User user = new User(
                expectedId,
                "f1 name",
                "l1 name",
                mockPasswordEncoder.encode("password"),
                null
        );

        Mockito.doReturn(user).when(mockUserRepository).save(Mockito.any(User.class));
        Mockito.doReturn(Optional.of(user)).when(mockUserRepository).findById(Mockito.anyLong());

        UserOutDto result = userService.add(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void find() throws NotFoundException {
        Long expectedId = 1L;

        Optional<User> user = Optional.of(new User(expectedId,
                "f1 name",
                "l1 name",
                mockPasswordEncoder.encode("password"),
                null
        ));

        Mockito.doReturn(user).when(mockUserRepository).findById(Mockito.anyLong());

        UserOutDto result = userService.findById(expectedId);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<User> user = Optional.empty();

        Mockito.doReturn(false).when(mockUserRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    userService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findAll() {
        userService.findAll();
        Mockito.verify(mockUserRepository).findAll();
    }

}