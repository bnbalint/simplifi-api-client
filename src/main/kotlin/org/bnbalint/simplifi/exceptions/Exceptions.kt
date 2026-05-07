package org.bnbalint.simplifi.exceptions

import org.bnbalint.simplifi.models.ErrorsResponse

/**
 * When we encounter a 429 error from SimpliFi we will retry
 * if we retry the max number of times and do not succeed, we throw this exception
 */
class SimplifiRateLimitRetriesExhausted(message: String, exception: Exception) : RuntimeException(message, exception)

/**
 * When we encounter a 5xx error from SimpliFi we will retry
 * if we retry the max number of times and do not succeed, we throw this exception
 */
class SimplifiServerExceptionRetriesExhausted(message: String, exception: Exception) : RuntimeException(message, exception)

/**
 * 422 Unprocessable Entity from SimpliFi contain an ErrorsResponse json
 * pass this message as the exception message
 */
class SimplifiUnprocessableEntityException(val errorsResponse: ErrorsResponse) :
    RuntimeException("422 Unprocessable Entity: ${errorsResponse.errors.firstOrNull() ?: "Unknown error"}")
