package ru.ylab.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ru.ylab.util.LiquibaseUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiquibaseInit {
    private final LiquibaseUtil liquibaseUtil;

    @EventListener
    public void seed(ContextRefreshedEvent event) {

        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> log.info("{}: {}", key, value));

        liquibaseUtil.init();
    }

}
