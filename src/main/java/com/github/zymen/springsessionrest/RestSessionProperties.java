package com.github.zymen.springsessionrest;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static java.util.Collections.singletonList;

@ConfigurationProperties("session-rest")
public class RestSessionProperties {

    /**
     * HTTP session timeout.
     */
    private int timeoutInSeconds = 30 * 60;
    /**
     * Properties responsible for persistent mode behaviour.
     */
    private Persistent persistent = new Persistent();

    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public void setTimeoutInSeconds(int timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public Persistent getPersistent() {
        return persistent;
    }

    public void setPersistent(Persistent persistent) {
        this.persistent = persistent;
    }

    public static class Persistent {

        /**
         * HTTP session application namespace under which session data must be stored.
         */
        private String namespace;
        /**
         * Couchbase cluster hosts.
         */
        private List<String> hosts = singletonList("localhost");

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public List<String> getHosts() {
            return hosts;
        }

        public void setHosts(List<String> hosts) {
            this.hosts = hosts;
        }
    }
}
