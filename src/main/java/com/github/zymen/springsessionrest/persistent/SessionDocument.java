package com.github.zymen.springsessionrest.persistent;

import java.util.Map;

public class SessionDocument {

    protected final String id;

    protected final Map<String, Map<String, Object>> data;

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
