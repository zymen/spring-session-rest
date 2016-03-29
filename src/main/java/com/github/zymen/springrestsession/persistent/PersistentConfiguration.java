package com.github.zymen.springrestsession.persistent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zymen.springrestsession.SessionCouchbaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

import java.util.List;

@Configuration("sessionPersistentConfiguration")
@EnableConfigurationProperties(SessionCouchbaseProperties.class)
@ConditionalOnProperty(name = "session-couchbase.in-memory.enabled", havingValue = "false", matchIfMissing = true)
public class PersistentConfiguration {

    @Autowired
    private SessionCouchbaseProperties sessionCouchbase;

//    @Bean
//    @ConditionalOnMissingBean
//    public CouchbaseDao couchbaseDao(CouchbaseTemplate couchbase) {
//        return new CouchbaseDao(couchbase);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MultiHttpSessionStrategy multiHttpSessionStrategy(CouchbaseDao dao, Serializer serializer) {
//        return new DelegatingSessionStrategy(new CookieHttpSessionStrategy(), dao, sessionCouchbase.getPersistent().getNamespace(), serializer);
//    }

    @Bean
    @ConditionalOnMissingBean
    public Serializer serializer() {
        return new Serializer();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public SessionRepository sessionRepository(CouchbaseDao dao, ObjectMapper mapper, Serializer serializer) {
//        return new CouchbaseSessionRepository(dao, sessionCouchbase.getPersistent().getNamespace(), mapper, sessionCouchbase.getTimeoutInSeconds(), serializer);
//    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
