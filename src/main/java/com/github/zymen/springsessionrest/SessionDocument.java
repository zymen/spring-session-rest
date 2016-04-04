package com.github.zymen.springsessionrest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDocument {

    protected final String id;

    protected final Map<String, Map<String, Object>> data;

    @JsonCreator
    public static SessionDocument create(Map<String, Object> rawObject) {
        String id = (String) rawObject.get("id");
        Map<String, Map<String, Object>> internalData = (Map<String, Map<String, Object>>) rawObject.get("data");

        return new SessionDocument(id, internalData);
    }

    public SessionDocument(String id, Map<String, Map<String, Object>> data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public Map<String, Map<String, Object>> getData() {
        return data;
    }
}
