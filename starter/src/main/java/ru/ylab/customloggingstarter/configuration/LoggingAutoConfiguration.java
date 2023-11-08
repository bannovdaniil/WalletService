package ru.ylab.customloggingstarter.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ylab.customloggingstarter.aop.aspects.ActionLoggingAspect;
import ru.ylab.customloggingstarter.aop.aspects.CommonPointcuts;
import ru.ylab.customloggingstarter.db.ConnectionManager;
import ru.ylab.customloggingstarter.db.impl.ConnectionManagerImpl;
import ru.ylab.customloggingstarter.repository.ActionRepository;
import ru.ylab.customloggingstarter.repository.impl.ActionRepositoryImpl;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {
    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration INIT!!!.");
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonPointcuts commonPointcuts() {
        return new CommonPointcuts();
    }

    @Bean
    @ConditionalOnMissingBean
    public ActionLoggingAspect actionLoggingAspect(ActionRepository actionRepository) {
        return new ActionLoggingAspect(actionRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ActionRepository getActionRepository(ConnectionManager connectionManager) {
        return new ActionRepositoryImpl(connectionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionManager actionRepository(DataSource dataSource) {
        return new ConnectionManagerImpl(dataSource);
    }

}
