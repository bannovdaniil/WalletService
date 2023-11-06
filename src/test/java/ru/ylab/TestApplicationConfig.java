package ru.ylab;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;


/**
 * Spring launcher for Tests
 */
@SpringBootTest
@Testcontainers
@Tag("DockerRequired")
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public abstract class TestApplicationConfig {
}
