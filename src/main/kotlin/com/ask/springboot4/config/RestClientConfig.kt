package com.ask.springboot4.config

import com.ask.springboot4.client.GitHubClient
import com.ask.springboot4.client.GoogleClient
import org.springframework.context.annotation.Configuration
import org.springframework.web.service.registry.ImportHttpServices

@Configuration
@ImportHttpServices(types = [GoogleClient::class])
@ImportHttpServices(types = [GitHubClient::class])
class RestClientConfig
