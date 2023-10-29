package ru.ylab.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.ylab.util.LiquibaseUtil;

@Component
@RequiredArgsConstructor
public class LiquibaseInit {
    private final LiquibaseUtil liquibaseUtil;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        liquibaseUtil.init();
    }
}
