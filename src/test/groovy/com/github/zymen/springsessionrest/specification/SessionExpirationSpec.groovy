package com.github.zymen.springsessionrest.specification

import com.github.zymen.springsessionrest.BasicSpec
import com.github.zymen.springsessionrest.Message
import org.springframework.test.context.TestPropertySource

import static com.github.zymen.springsessionrest.assertions.Assertions.assertThat

@TestPropertySource(properties = ['session-couchbase.timeout-in-seconds: 1'])
abstract class SessionExpirationSpec extends BasicSpec {

    def "Should not get HTTP session attribute when session has expired"() {
        given:
        def message = new Message(text: 'power rangers', number: 123)
        setSessionAttribute message
        sleep(sessionTimeout + 100)

        when:
        def response = getSessionAttribute()

        then:
        assertThat(response)
                .hasNoBody()
    }
}