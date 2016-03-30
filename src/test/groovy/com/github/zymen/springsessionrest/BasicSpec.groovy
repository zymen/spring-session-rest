package com.github.zymen.springsessionrest

import com.github.zymen.springsessionrest.testapp.Message
import com.github.zymen.springsessionrest.testapp.TestApplication
import com.github.zymen.springsessionrest.utils.ApplicationInstance
import com.github.zymen.springsessionrest.utils.ApplicationInstanceRunner
import com.github.zymen.springsessionrest.utils.SessionRestInstanceRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

import static java.net.HttpCookie.parse
import static org.springframework.http.HttpHeaders.COOKIE
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.PUT

@WebIntegrationTest(randomPort = true)
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = TestApplication)
abstract class BasicSpec extends Specification {

    @Shared
    private RestTemplate rest = new RestTemplate()

    @Autowired
    private EmbeddedWebApplicationContext context
    private int extraInstancePort
    private ApplicationInstance instance
    private ApplicationInstance sessionRestApplicationInstance

    @Autowired
    private RestSessionProperties sessionRestProperties
    // Cannot store cookie in thread local because some tests starts more than one app instance. CANNOT run tests in parallel.
    private String currentSessionCookie

    void setup() {
        startSessionRestInstance()
    }

    void cleanup() {
        clearSessionCookie()
        stopExtraApplicationInstance()
        stopSessionRestInstance()
    }

    protected void startSessionRestInstance(String namespace = sessionRestProperties.persistent.namespace) {
        URL[] urls = [new File('/build/classes/test').toURI().toURL()]
        def classLoader = new URLClassLoader(urls, getClass().classLoader)
        def runnerClass = classLoader.loadClass(SessionRestInstanceRunner.class.name)
        def runnerInstance = runnerClass.newInstance()
        sessionRestApplicationInstance = new ApplicationInstance(runnerClass, runnerInstance)
//        runnerClass.getMethod('setNamespace', String).invoke(runnerInstance, namespace)
        runnerClass.getMethod('run').invoke(runnerInstance)
    }

    protected void stopSessionRestInstance() {
        if (sessionRestApplicationInstance) {
            sessionRestApplicationInstance.runnerClass.getMethod('stop').invoke(sessionRestApplicationInstance.runnerInstance)
            sessionRestApplicationInstance = null
        }
    }

    protected void startExtraApplicationInstance(String namespace = sessionRestProperties.persistent.namespace) {
        URL[] urls = [new File('/build/classes/test').toURI().toURL()]
        def classLoader = new URLClassLoader(urls, getClass().classLoader)
        def runnerClass = classLoader.loadClass(ApplicationInstanceRunner.class.name)
        def runnerInstance = runnerClass.newInstance()
        instance = new ApplicationInstance(runnerClass, runnerInstance)
        runnerClass.getMethod('setNamespace', String).invoke(runnerInstance, namespace)
        runnerClass.getMethod('run').invoke(runnerInstance)
        extraInstancePort = runnerClass.getMethod('getPort').invoke(runnerInstance) as int
    }

    protected void stopExtraApplicationInstance() {
        if (instance) {
            instance.runnerClass.getMethod('stop').invoke(instance.runnerInstance)
            instance = null
        }
    }

    protected int getSessionTimeout() {
        return sessionRestProperties.timeoutInSeconds * 1000
    }

    protected void setSessionAttribute(Message attribute) {
        post('session/attribute', attribute, getPort())
    }

    protected void setGlobalSessionAttribute(Message attribute) {
        post('session/attribute/global', attribute, getPort())
    }

    protected void setSessionAttributeToExtraInstance(Message attribute) {
        post('session/attribute', attribute, extraInstancePort)
    }

    protected void deleteSessionAttribute() {
        delete('session/attribute', getPort())
    }

    protected void deleteGlobalSessionAttribute() {
        delete('session/attribute/global', getPort())
    }

    protected ResponseEntity<Message> getSessionAttribute() {
        return get('session/attribute', Message, getPort())
    }

    protected ResponseEntity<Message> getSessionAttributeFromExtraInstance() {
        return get('session/attribute', Message, extraInstancePort)
    }

    protected ResponseEntity<Message> getGlobalSessionAttributeFromExtraInstance() {
        return get('session/attribute/global', Message, extraInstancePort)
    }

    protected void setSessionBean(Message attribute) {
        post('session/bean', attribute)
    }

    protected ResponseEntity<Message> getSessionBean() {
        return get('session/bean', Message)
    }

    protected void invalidateSession() {
        delete('session')
    }

    protected void changeSessionId() {
        put('session/id')
    }

    protected String setPrincipalSessionAttribute() {
        return post('session/principal', null, getPort(), String).body
    }

    protected String setPrincipalSessionAttributeToExtraInstance() {
        return post('session/principal', null, extraInstancePort, String).body
    }

    protected ResponseEntity<Set<String>> getPrincipalSessions() {
        return get('session/principal', Set, getPort())
    }

    protected void clearSessionCookie() {
        currentSessionCookie = null
    }

    private String getCurrentSessionId() {
        return parse(currentSessionCookie)[0].value
    }

    private <T> ResponseEntity<T> post(String path, Object body, int port = getPort(), Class<T> responseType = Object) {
        def url = createUrl(path, port)
        HttpHeaders headers = addSessionCookie()
        def request = new HttpEntity<>(body, headers)
        def response = rest.postForEntity(url, request, responseType)
        saveSessionCookie(response)
        return response
    }

    private <T> ResponseEntity<T> get(String path, Class<T> responseType, int port = getPort()) {
        def url = createUrl(path, port)
        HttpHeaders headers = addSessionCookie()
        def request = new HttpEntity<>(headers)
        def response = rest.exchange(url, GET, request, responseType) as ResponseEntity<T>
        saveSessionCookie(response)
        return response
    }

    private void delete(String path, int port = getPort()) {
        def url = createUrl(path, port)
        HttpHeaders headers = addSessionCookie()
        def request = new HttpEntity<>(headers)
        def response = rest.exchange(url, DELETE, request, Object)
        saveSessionCookie(response)
    }

    private ResponseEntity<Object> put(String path, int port = getPort()) {
        def url = createUrl(path, port)
        HttpHeaders headers = addSessionCookie()
        def request = new HttpEntity<>(headers)
        def response = rest.exchange(url, PUT, request, Object)
        saveSessionCookie(response)
        return response
    }

    private static GString createUrl(String path, int port) {
        return "http://localhost:$port/$path"
    }

    private int getPort() {
        return context.embeddedServletContainer.port
    }

    private HttpHeaders addSessionCookie() {
        def headers = new HttpHeaders()
        headers.set(COOKIE, currentSessionCookie)
        return headers
    }

    private void saveSessionCookie(ResponseEntity response) {
        if (!response.headers.containsKey("Set-Cookie")) {
            return
        }

        def cookie = response.headers
                .getFirst("Set-Cookie")
                .split(";")[0]

        if (cookie != null) {
            currentSessionCookie = cookie
        }
    }
}
