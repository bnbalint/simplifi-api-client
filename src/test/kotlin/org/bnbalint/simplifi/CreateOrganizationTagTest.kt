package org.bnbalint.simplifi

import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CreateOrganizationTagTest {

    @Test
    fun test_notImplemented() {
        val server = MockWebServer()

        val client = SimplifiApiClient(server.url("/").toString(), "app", "user", 3, 1, 1, 3, 1, 1)
        assertThrows(NotImplementedError::class.java) { client.createOrganizationTag() }
    }
}
