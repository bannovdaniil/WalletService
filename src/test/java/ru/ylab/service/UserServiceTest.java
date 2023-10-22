package ru.ylab.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.impl.UserServiceImpl;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

import java.lang.reflect.Field;
import java.util.Optional;


class UserServiceTest {
    private static UserService userService;
    private static UserRepository mockUserRepository;
    private static WalletRepository mockWalletRepository;
    private static UserRepositoryImpl oldInstanceUserRepositoryImpl;
    private static UserRepository oldInstanceUserRepository;
    private static WalletRepositoryImpl oldWalletRepositoryInstance;
    private static UserServiceImpl oldUserServiceImpl;
    private static PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();

    private static void setMock(UserRepository mock) {
        try {
            Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstanceUserRepositoryImpl = (UserRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(WalletRepository mock) {
        try {
            Field instance = WalletRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldWalletRepositoryInstance = (WalletRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException, IllegalAccessException {
        mockUserRepository = Mockito.mock(UserRepository.class);
        setMock(mockUserRepository);
        mockWalletRepository = Mockito.mock(WalletRepository.class);
        setMock(mockWalletRepository);

        Field instance = UserServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        oldUserServiceImpl = (UserServiceImpl) instance.get(instance);

        userService = UserServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstanceUserRepositoryImpl);

        instance = WalletRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldWalletRepositoryInstance);

        instance = UserServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldUserServiceImpl);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
        Mockito.reset(mockWalletRepository);
    }

    @Test
    void add() throws NotFoundException {
        Long expectedId = 1L;
        UserIncomingDto dto = new UserIncomingDto("f1 name", "l1 name", "password");
        User user = new User(
                expectedId,
                "f1 name",
                "l1 name",
                passwordEncoder.encode("password"),
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
                passwordEncoder.encode("password"),
                null
        ));

        Mockito.doReturn(user).when(mockUserRepository).findById(Mockito.anyLong());

        User result = userService.findById(expectedId);

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