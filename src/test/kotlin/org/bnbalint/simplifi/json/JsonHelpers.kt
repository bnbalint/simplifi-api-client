package org.bnbalint.simplifi.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.IOException

/**
 * These are used for API serialization/deserialization tests
 */
object JsonHelpers {
    private val MAPPER = ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(JavaTimeModule())
        // Kotlin module needed for constructor parameter names & default values
        .registerModule(KotlinModule.Builder().build())


    @Throws(IOException::class)
    private fun getFileContents(filename: String): String {
        return this::class.java.getResource(filename)?.readText(Charsets.UTF_8) ?: ""
    }

    @Throws(IOException::class)
    fun normalizedJson(filename: String): String {
        val contents = getFileContents(filename)
        val node = MAPPER.readValue(contents, JsonNode::class.java)
        return MAPPER.writeValueAsString(node)
    }

    @Throws(IOException::class)
    fun <T> readObject(filename: String, clazz: Class<T>?): T {
        val contents = getFileContents(filename)
        return MAPPER.readValue(contents, clazz)
    }

    @Throws(JsonProcessingException::class)
    fun getJson(value: Any?): String {
        return MAPPER.writeValueAsString(value)
    }
}
