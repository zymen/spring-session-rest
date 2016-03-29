package com.github.zymen.springsessionrest.specification

import com.github.zymen.springsessionrest.BasicSpec
import spock.lang.Ignore

import static com.github.zymen.springsessionrest.assertions.Assertions.assertThat

@Ignore
class PrincipalSessionsSpec extends BasicSpec {

    def "Should get principal sessions when they exist"() {
        given:
        def firstSessionId = setPrincipalSessionAttribute()
        clearSessionCookie()
        startExtraApplicationInstance()
        def secondSessionId = setPrincipalSessionAttributeToExtraInstance()

        when:
        def response = getPrincipalSessions()

        then:
        assertThat(response)
                .hasSessionIds(firstSessionId, secondSessionId)
    }

    def "Should not get principal session when it was invalidated"() {
        given:
        setPrincipalSessionAttribute()

        when:
        invalidateSession()

        then:
        assertThat(getPrincipalSessions())
                .hasNoSessionIds()
    }
}