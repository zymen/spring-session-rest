package com.github.zymen.springsessionrest.principalsessions;

import java.util.Collections;
import java.util.List;

public class PrincipalSessionsDocument {

    protected final String principal;

    protected final List<String> sessionIds;

    public PrincipalSessionsDocument(String principal, List<String> sessionIds) {
        this.principal = principal;
        this.sessionIds = sessionIds;
    }

    public String getPrincipal() {
        return principal;
    }

    public List<String> getSessionIds() {
        return sessionIds == null ? Collections.<String>emptyList() : sessionIds;
    }
}
