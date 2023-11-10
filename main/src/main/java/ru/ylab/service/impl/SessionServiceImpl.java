package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ylab.model.Session;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.repository.SessionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.SessionService;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Бизнес логика Action Событий которые делает пользователь.
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (passwordEncoder.matches(dto.getPassword(), user.getHashPassword())) {
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
    public Optional<UUID> getUuidFromCookie(String cookies) {
        Optional<UUID> uuid = Optional.empty();
        if (cookies != null && !cookies.isBlank()) {
            uuid = Optional.of(UUID.fromString(cookies));
        }
        return uuid;
    }
}
