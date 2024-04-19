package ua.com.radiokot.license.service.util

import org.junit.Assert
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

class RequestRateLimiterTest {

    @Test
    fun haveNoWait_IfFirstRequest() {
        val rateLimiter = RequestRateLimiter("A", 1000L)

        val elapsed = measureTimeMillis {
            rateLimiter.waitBeforeRequest()
        }

        Assert.assertTrue(
            "There must be no significant delay for a single first request",
            elapsed <= 50
        )
    }

    @Test
    fun haveToWait_IfRequestingInSeries() {
        val rateLimiter = RequestRateLimiter("A", 500L)
        val requestCount = 5
        val elapsed = measureTimeMillis {
            repeat(requestCount) {
                rateLimiter.waitBeforeRequest()
            }
        }
        val expectedDelay = (requestCount - 1) * rateLimiter.requestTimeoutMs
        Assert.assertTrue(
            "There must be $expectedDelay ms delay for $requestCount requests",
            (expectedDelay - elapsed).absoluteValue <= 50
        )
    }
}
