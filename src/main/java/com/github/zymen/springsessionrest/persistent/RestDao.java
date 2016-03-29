package com.github.zymen.springsessionrest.persistent;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RestDao {

    private static final Logger log = getLogger(RestDao.class);

    private String sessionUrl = "http://localhost:10080/session";

    private RestTemplate restTemplate = new RestTemplate();

    public SessionDocument findById(String requestedSessionId) {
        return null;
    }

    public void delete(String id) {
    }

    public void save(SessionDocument newDocument) {
        ResponseEntity response = restTemplate.postForEntity(sessionUrl, newDocument, newDocument.getClass());
        log.info(response.getStatusCode().toString());
    }

    public void updateExpirationTime(String id, int sessionDocumentExpiration) {

    }
}
