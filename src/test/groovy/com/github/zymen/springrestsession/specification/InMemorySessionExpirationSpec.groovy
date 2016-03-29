package com.github.zymen.springrestsession.specification

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ['session-couchbase.in-memory.enabled: true'])
class InMemorySessionExpirationSpec extends SessionExpirationSpec {
}