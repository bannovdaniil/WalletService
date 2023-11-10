package ru.ylab.customenableannotation.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import ru.ylab.customenableannotation.aop.aspects.CustomLogger;
import ru.ylab.customenableannotation.aop.aspects.EnableCommonPointcuts;

@Slf4j
public class EnableCustomLoggerAutoConfiguration {
    @PostConstruct
    void init() {
        log.info("Enable Custom Logger AutoConfiguration INIT!!!.");
    }

    @Bean
    @ConditionalOnMissingBean
    public EnableCommonPointcuts enableCommonPointcuts() {
        return new EnableCommonPointcuts();
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomLogger customLogger() {
        return new CustomLogger();
    }
}
