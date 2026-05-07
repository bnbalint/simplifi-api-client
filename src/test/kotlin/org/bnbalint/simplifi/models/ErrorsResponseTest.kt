package org.bnbalint.simplifi.models

import org.bnbalint.simplifi.json.JsonHelpers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

/**
 * Response types will only ever be deserialized (received from API)
 */
class ErrorsResponseTest {

    @Test
    fun deserializes_from_json() {
        val response = JsonHelpers.readObject("/models/errors_response.json", ErrorsResponse::class.java)
        assertThat(response.errors.size, `is`(1))
        assertThat(response.errors[0], `is`("Name has already been taken"))
    }
}
