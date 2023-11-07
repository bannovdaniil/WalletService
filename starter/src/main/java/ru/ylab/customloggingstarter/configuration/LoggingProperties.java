package ru.ylab.customloggingstarter.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Slf4j
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.common.logging")
public class LoggingProperties {
    /**
     * enable - logging Annotation
     */
    private boolean enabled;
    /**
     * level - INFO, DEBUG, ERROR
     */
    private String level;

    @PostConstruct
    void init() {
        log.info("LoggingProperties init. {}", this);
    }
}
