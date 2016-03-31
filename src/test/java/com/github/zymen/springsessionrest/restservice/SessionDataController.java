package com.github.zymen.springsessionrest.restservice;

import com.github.zymen.springsessionrest.persistent.SessionDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/sessions")
public class SessionDataController {

    private final ConcurrentMap<String, Object> sessions;

    public SessionDataController() {
        sessions = new ConcurrentHashMap<>();
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getSession(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok(sessions.get(sessionId));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void saveSession(@RequestBody SessionDocument sessionDocument) {
        sessions.put(sessionDocument.getId(), sessionDocument);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    public void deleteSession(@PathVariable("sessionId") String sessionId) {
        sessions.remove(sessionId);
    }
}
