package ru.ylab;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import ru.ylab.configuration.WebConfig;

@Configuration
@ComponentScan(
        value = "ru.ylab",
        excludeFilters = {@ComponentScan.Filter(value = WebConfig.class, type = FilterType.ASSIGNABLE_TYPE)}
)
public class TestApplicationConfig {
}
