package com.ask.springboot4.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/versioning")
class VersioningController {
  @GetMapping
  fun v1() = mapOf(
    "version" to "no version",
    "timestamp" to Instant.now(),
  )

  @GetMapping(version = "1.1")
  fun v2() = mapOf(
    "version" to "1.1",
    "timestamp" to Instant.now(),
  )

  @GetMapping(version = "1.2+")
  fun v3() = mapOf(
    "version" to "1.2+",
    "timestamp" to Instant.now(),
  )

  @GetMapping(version = "1.4")
  fun v4() = mapOf(
    "version" to "1.4",
    "timestamp" to Instant.now(),
  )
}
