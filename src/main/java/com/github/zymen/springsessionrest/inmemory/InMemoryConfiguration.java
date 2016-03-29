package com.github.zymen.springsessionrest.inmemory;

import com.github.zymen.springsessionrest.RestSessionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;

@Configuration("sessionInMemoryConfiguration")
@EnableConfigurationProperties(RestSessionProperties.class)
@ConditionalOnProperty(name = "session-rest.in-memory.enabled")
public class InMemoryConfiguration {

    @Autowired
    private RestSessionProperties restSessionProperties;

    @Bean
    @ConditionalOnMissingBean
    public SessionRepository sessionRepository() {
        MapSessionRepository repository = new MapSessionRepository();
        repository.setDefaultMaxInactiveInterval(restSessionProperties.getTimeoutInSeconds());
        return repository;
    }
}
