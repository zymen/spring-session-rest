package com.github.zymen.springsessionrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zymen.springsessionrest.persistent.RestDao;
import com.github.zymen.springsessionrest.persistent.RestSessionRepository;
import com.github.zymen.springsessionrest.persistent.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "session-rest.in-memory.enabled", havingValue = "false", matchIfMissing = true)
public class ImmediateSessionExpirationRepository extends RestSessionRepository {

    @Autowired
    public ImmediateSessionExpirationRepository(RestDao dao, ObjectMapper mapper, Serializer serializer, RestSessionProperties restSessionProperties) {
        super(dao, restSessionProperties.getPersistent().getNamespace(), mapper, restSessionProperties.getTimeoutInSeconds(), serializer);
    }

    @Override
    protected int getSessionDocumentExpiration() {
        return sessionTimeout;
    }
}
