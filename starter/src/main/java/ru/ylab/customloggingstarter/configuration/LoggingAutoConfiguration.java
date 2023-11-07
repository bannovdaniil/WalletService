package ru.ylab.customloggingstarter.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ylab.customloggingstarter.aop.aspects.ActionLoggingAspect;
import ru.ylab.customloggingstarter.aop.aspects.CommonPointcuts;

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
    public CommonPointcuts commonPointcuts() {
        return new CommonPointcuts();
    }

    @Bean
    public ActionLoggingAspect actionLoggingAspect() {
        return new ActionLoggingAspect();
    }
}
