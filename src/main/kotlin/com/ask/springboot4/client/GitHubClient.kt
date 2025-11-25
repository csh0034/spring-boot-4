package com.ask.springboot4.client

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange(url = "https://github.com")
interface GitHubClient {
  @GetExchange("/")
  fun index(): String
}
