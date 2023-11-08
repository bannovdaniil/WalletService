package ru.ylab.customenableannotation.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ylab.customenableannotation.aop.aspects.EnableCommonPointcuts;

@Slf4j
@Configuration
public class EnableCustomLoggerAutoConfiguration {
    @PostConstruct
    void init() {
        log.info("Enable CustomLoggerAutoConfiguration INIT!!!.");
    }

    @Bean
    @ConditionalOnMissingBean
    public EnableCommonPointcuts enableCommonPointcuts() {
        return new EnableCommonPointcuts();
    }
}
