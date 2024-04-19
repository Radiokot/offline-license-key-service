package ua.com.radiokot.license.service.util

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Limits requests rate by providing timeouts for requestors.
 *
 * @param resourceName name of the requested resource, used for logging.
 * @param requestTimeoutMs how long to wait after each request before allowing the next one.
 *
 * @see waitBeforeRequest
 */
class RequestRateLimiter(
    resourceName: String,
    val requestTimeoutMs: Long,
) {
    private val queue = mutableListOf<CountDownLatch>()
    private val executor = Executors.newSingleThreadScheduledExecutor {
        Thread(it).apply {
            name = "${resourceName}RateLimiterThread"
            isDaemon = true
        }
    }

    /**
     * Suspends current thread for a time required to safely
     * perform Strava request
     */
    fun waitBeforeRequest() = synchronized(queue) {
        val currentLatch = queue.lastOrNull() ?: CountDownLatch(0)

        val latchForNext = CountDownLatch(1)
        queue.add(latchForNext)

        val waitFor = requestTimeoutMs * queue.size

        executor.schedule({
            queue.remove(latchForNext)
            latchForNext.countDown()
        }, waitFor, TimeUnit.MILLISECONDS)

        currentLatch.await()
    }
}
