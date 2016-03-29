package com.github.zymen.springsessionrest.specification

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ['session-couchbase.in-memory.enabled: true'])
class InMemorySessionSpec extends SessionSpec {
}