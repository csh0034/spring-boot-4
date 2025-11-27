package com.ask.springboot4

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.resilience.annotation.ConcurrencyLimit
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class ResilienceTest {
  @Autowired
  private lateinit var resilienceService: ResilienceService

  @Test
  fun retry() {
    resilienceService.retry()
  }

  @Test
  fun limit() {
    val executor = Executors.newVirtualThreadPerTaskExecutor()
    val countDownLatch = CountDownLatch(3)

    repeat(3) {
      executor.submit {
        resilienceService.limit()
        countDownLatch.countDown()
      }
    }

    countDownLatch.await()
  }
}

@Service
class ResilienceService {
  private val log = LoggerFactory.getLogger(javaClass)

  @Retryable(
    maxRetries = 4,
    delay = 100,
    jitter = 10,
    multiplier = 2.0,
    maxDelay = 1000
  )
  fun retry() {
    throw IllegalStateException("retry...")
  }

  @ConcurrencyLimit(1)
  fun limit() {
    log.info("invoked...")
    Thread.sleep(3000)
  }
}
