package com.github.zymen.springrestsession.specification

import com.github.zymen.springrestsession.BasicSpec

import static com.github.zymen.springrestsession.assertions.Assertions.assertThat

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