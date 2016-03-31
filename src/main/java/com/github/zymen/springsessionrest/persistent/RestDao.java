package com.github.zymen.springsessionrest.persistent;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RestDao {

    private static final Logger log = getLogger(RestDao.class);

    private String sessionUrl = "http://localhost:10080/sessions";

    private RestTemplate restTemplate = new RestTemplate();

    public SessionDocument findById(String requestedSessionId) {
        log.info("findById " + requestedSessionId);

        try {
            URI requestUrl = new URI(sessionUrl + "/" + requestedSessionId);
            ResponseEntity<SessionDocument> sessionDocument = restTemplate.getForEntity(requestUrl, SessionDocument.class);

            return sessionDocument.getBody();
        } catch (URISyntaxException e) {
            log.error("errorr", e);
        }

        return null;
    }

    public void updateSession(Map<String, Object> attributes, String namespace, String id) {
        log.info("updateSession " + id);

        SessionDocument sessionDocument = findById(id);
        sessionDocument.getData().put(namespace, attributes);
        save(sessionDocument);
    }

    public void updateAppendPrincipalSession(String principal, String sessionId) {
    }

    public void updateRemovePrincipalSession(String principal, String sessionId) {
    }

    public void delete(String id) {
        try {
            URI requestUrl = new URI(sessionUrl + "/" + id);
            restTemplate.delete(requestUrl);
        } catch (URISyntaxException e) {
            log.error("Problems with deleting session", e);
        }
    }

    public void save(SessionDocument newDocument) {
        ResponseEntity response = restTemplate.postForEntity(sessionUrl, newDocument, newDocument.getClass());
        log.info(response.getStatusCode().toString());
    }

    public void updateExpirationTime(String id, int sessionDocumentExpiration) {

    }

    public void save(PrincipalSessionsDocument sessionsDocument) {
    }

    public boolean exists(String principal) {
        log.info("exists " + principal);
        return false;
    }

    public Map<String, Object> findSessionAttributes(String sessionId, String namespace) {
        SessionDocument session = findById(sessionId);
        if (session == null) {
            return null;
        }

        return session.getData().get(namespace);
    }
}
