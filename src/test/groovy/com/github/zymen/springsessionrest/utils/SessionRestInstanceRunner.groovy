package com.github.zymen.springsessionrest.utils

import com.github.zymen.springsessionrest.restservice.SessionRestApplication
import com.github.zymen.springsessionrest.testapp.TestApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext

import static org.springframework.boot.SpringApplication.exit

class SessionRestInstanceRunner {

    private final Object monitor = new Object()
    private EmbeddedWebApplicationContext context
    private boolean shouldWait
    private String namespace
    private int port

    void run() {
        if (context != null) {
            throw new IllegalStateException('Application context must be null to run this instance')
        }
        runInstance()
        waitInstanceIsStarted()
    }

    void stop() {
        exit(context)
        context = null
    }

    void setNamespace(String namespace) {
        this.namespace = namespace
    }

    int getPort() {
        return port
    }

    private void waitInstanceIsStarted() {
        synchronized (monitor) {
            if (shouldWait) {
                monitor.wait()
            }
        }
    }

    private void runInstance() {
        def runnerThread = new InstanceRunningThread()
        shouldWait = true
        runnerThread.contextClassLoader = SessionRestApplication.classLoader
        runnerThread.start()
    }

    private class InstanceRunningThread extends Thread {

        @Override
        public void run() {
            context = SpringApplication.run(SessionRestApplication, '--server.port=10080') as EmbeddedWebApplicationContext
            port = context.embeddedServletContainer.port
            synchronized (monitor) {
                shouldWait = false
                monitor.notify()
            }
        }
    }
}
