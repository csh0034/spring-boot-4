package com.ask.springboot4.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.service.registry.ImportHttpServices

@Configuration
@ImportHttpServices(basePackages = ["com.ask.springboot4.client"])
class RestClientConfig
