package com.github.zymen.springsessionrest.specification

import com.github.zymen.springsessionrest.BasicSpec
import com.github.zymen.springsessionrest.testapp.Message
import spock.lang.Ignore

import static com.github.zymen.springsessionrest.assertions.Assertions.assertThat

class NamespacesSpec extends BasicSpec {

    def "Should set and get global HTTP session attribute using the same namespace"() {
        given:
        def message = new Message(text: 'i robot 1', number: 1)
        setGlobalSessionAttribute message
        startExtraApplicationInstance()

        when:
        def response = getGlobalSessionAttributeFromExtraInstance()

        then:
        assertThat(response)
                .hasBody(message)
    }

    def "Should set and get HTTP session attribute using the same namespace"() {
        given:
        def message = new Message(text: 'i robot 2', number: 2)
        setSessionAttribute message
        startExtraApplicationInstance()

        when:
        def response = getSessionAttributeFromExtraInstance()

        then:
        assertThat(response)
                .hasBody(message)
    }

    def "Should set and get global HTTP session attribute using different namespace"() {
        given:
        def message = new Message(text: 'i robot 3', number: 3)
        setGlobalSessionAttribute message
        startExtraApplicationInstance('other_namespace')

        when:
        def response = getGlobalSessionAttributeFromExtraInstance()

        then:
        assertThat(response)
                .hasBody(message)
    }

    @Ignore
    def "Should not get HTTP session attribute using different namespace"() {
        given:
        def message = new Message(text: 'i robot 4', number: 4)
        setSessionAttribute message
        startExtraApplicationInstance('other_namespace')

        when:
        def response = getSessionAttributeFromExtraInstance()

        then:
        assertThat(response)
                .hasNoBody()
    }

    @Ignore
    def "Should set and remove global HTTP session attribute using different namespace"() {
        given:
        def message = new Message(text: 'delete me', number: 71830)
        setGlobalSessionAttribute message
        startExtraApplicationInstance('other_namespace')

        when:
        deleteGlobalSessionAttribute()

        then:
        assertThat(getGlobalSessionAttributeFromExtraInstance())
                .hasNoBody()
    }
}