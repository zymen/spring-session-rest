package com.github.zymen.springrestsession.specification

import com.github.zymen.springrestsession.Message

import static com.github.zymen.springrestsession.assertions.Assertions.assertThat

class PersistentSessionExpirationSpec extends SessionExpirationSpec {

    def "Should not get Couchbase session document when session has expired"() {
        given:
        def message = new Message(text: 'power rangers 2', number: 10001)
        setSessionAttribute message

        when:
        sleep(sessionTimeout + 1000)

        then:
        !currentSessionExists()
    }

    def "Should not get Couchbase principal sessions document when sessions have expired"() {
        given:
        setPrincipalSessionAttribute()

        when:
        sleep(sessionTimeout + 1000)

        then:
        assertThat(getPrincipalSessions())
                .hasNoSessionIds()
    }
}