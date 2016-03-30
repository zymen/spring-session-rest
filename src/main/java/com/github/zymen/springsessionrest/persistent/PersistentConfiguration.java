package com.github.zymen.springsessionrest.persistent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zymen.springsessionrest.RestSessionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

@Configuration("sessionPersistentConfiguration")
@EnableConfigurationProperties(RestSessionProperties.class)
public class PersistentConfiguration {

    @Autowired
    private RestSessionProperties restSessionProperties;
//
    @Bean
    @ConditionalOnMissingBean
    public RestDao restDao() {
        return new RestDao();
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiHttpSessionStrategy multiHttpSessionStrategy(RestDao dao, Serializer serializer) {
        return new DelegatingSessionStrategy(new CookieHttpSessionStrategy(), dao, restSessionProperties.getPersistent().getNamespace(), serializer);
    }

    @Bean
    @ConditionalOnMissingBean
    public Serializer serializer() {
        return new Serializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionRepository sessionRepository(RestDao dao, ObjectMapper mapper, Serializer serializer) {
        return new RestSessionRepository(dao, restSessionProperties.getPersistent().getNamespace(), mapper, restSessionProperties.getTimeoutInSeconds(), serializer);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
