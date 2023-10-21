package ru.ylab.service.impl;

import jakarta.servlet.http.Cookie;
import ru.ylab.model.Session;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.repository.SessionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.SessionRepositoryImpl;
import ru.ylab.repository.impl.UserRepositoryImpl;
import ru.ylab.repository.impl.WalletRepositoryImpl;
import ru.ylab.service.SessionService;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static ru.ylab.Constants.SESSION_COOKIE;

/**
 * Бизнес логика Action Событий которые делает пользователь.
 */
public class SessionServiceImpl implements SessionService {
    private static SessionService instance;
    private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();


    private SessionServiceImpl() {
    }

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionServiceImpl();
        }
        return instance;
    }

    @Override
    public User getUser(UUID sessionId) {
        User user = null;
        if (isActive(sessionId)) {
            Session session = sessionRepository.findById(sessionId).orElseThrow();
            user = userRepository.findById(session.getUserId()).orElseThrow();
        }
        return user;
    }

    @Override
    public UUID login(UserLoginDto dto) throws AccessDeniedException {
        UUID sessionId;
        if (dto.getUserId() == null || dto.getPassword() == null) {
            throw new InvalidParameterException("Wrong Password or UserId.");
        }

        User user = userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new AccessDeniedException("Wrong.")
        );

        if (passwordEncoder.encode(dto.getPassword()).equals(user.getHashPassword())) {
            Session session = new Session(
                    LocalDateTime.now(),
                    dto.getUserId()
            );
            sessionId = sessionRepository.save(session).getUuid();
        } else {
            throw new AccessDeniedException("Wrong.");
        }

        return sessionId;
    }

    @Override
    public void logout(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Override
    public boolean isActive(UUID sessionId) {
        return sessionRepository.isActive(sessionId);
    }

    @Override
    public Optional<UUID> getUuidFromCookie(Cookie[] cookies) {
        Optional<UUID> uuid = Optional.empty();

        Optional<String> cookieValue = Arrays.stream(cookies)
                .filter(cookie -> SESSION_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
        if (cookieValue.isPresent()) {
            uuid = Optional.ofNullable(UUID.fromString(cookieValue.get()));
        }
        return uuid;
    }

}
