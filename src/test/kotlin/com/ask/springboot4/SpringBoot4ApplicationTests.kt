package com.ask.springboot4

import com.ask.springboot4.client.GoogleClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringBoot4ApplicationTests {
  @Autowired
  private lateinit var googleClient: GoogleClient

  @Test
  fun contextLoads() {
  }
}
