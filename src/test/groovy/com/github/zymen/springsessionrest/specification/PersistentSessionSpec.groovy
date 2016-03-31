package com.github.zymen.springsessionrest.specification

import com.github.zymen.springsessionrest.testapp.Message

import static com.github.zymen.springsessionrest.assertions.Assertions.assertThat

class PersistentSessionSpec extends SessionSpec {

    def "Should copy HTTP session attributes when session ID was changed"() {
        given:
        def message = new Message(text: 'i cannot disappear!', number: 13)
        setSessionAttribute message
        def globalMessage = new Message(text: 'i cannot disappear too!', number: 12222)
        setGlobalSessionAttribute globalMessage
        startExtraApplicationInstance('wicked_application')
        def extraMessage = new Message(text: 'and me too!', number: 14100)
        setSessionAttributeToExtraInstance extraMessage

        when:
        changeSessionId()

        then:
        assertThat(getSessionAttribute())
                .hasBody(message)
        assertThat(getGlobalSessionAttributeFromExtraInstance())
                .hasBody(globalMessage)
        assertThat(getSessionAttributeFromExtraInstance())
                .hasBody(extraMessage)
    }
}