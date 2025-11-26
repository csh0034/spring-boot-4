package com.ask.springboot4

import com.ask.springboot4.client.GitHubClient
import com.ask.springboot4.client.GoogleClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HttpExchangeTest {
  @Autowired
  private lateinit var googleClient: GoogleClient

  @Autowired
  private lateinit var gitHubClient: GitHubClient

  @Test
  fun googleClient() {
    googleClient.index()
  }

  @Test
  fun gitHubClient() {
    gitHubClient.index()
  }
}
