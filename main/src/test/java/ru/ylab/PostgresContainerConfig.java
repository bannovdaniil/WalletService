package ru.ylab;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfig {
    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(
            @Value("${spring.datasource.username}") String db_username,
            @Value("${spring.datasource.password}") String db_password,
            @Value("${spring.datasource.containerPort}") int containerPort,
            @Value("${spring.datasource.localPort}") int localPort) {
        return new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("wallet_db")
                .withUsername(db_username)
                .withPassword(db_password)
                .withExposedPorts(containerPort)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                ));
    }
}
