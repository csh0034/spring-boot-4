package com.ask.springboot4.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain = http
    .csrf { it.disable() }
    .httpBasic { it.disable() }
    .logout { it.disable() }
    .authorizeHttpRequests { it.requestMatchers("/**").permitAll() }
    .build()
}
