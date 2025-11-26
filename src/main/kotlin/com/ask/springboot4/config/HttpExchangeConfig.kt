package com.ask.springboot4.config

import com.ask.springboot4.client.GitHubClient
import com.ask.springboot4.client.GoogleClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer
import org.springframework.web.service.registry.HttpServiceGroup
import org.springframework.web.service.registry.ImportHttpServices

@Configuration
// @ImportHttpServices(basePackages = ["com.ask.springboot4.client"]) // group 구분 안할 경우
@ImportHttpServices(group = "google", types = [GoogleClient::class])
@ImportHttpServices(group = "github", types = [GitHubClient::class])
class HttpExchangeConfig {
  @Bean
  fun defaultGroupConfigurer(): RestClientHttpServiceGroupConfigurer {
    return RestClientHttpServiceGroupConfigurer { groups ->
      groups.forEachClient { group, builder ->
        builder.defaultStatusHandler(HttpStatusCode::isError, RestClientErrorHandler(group.exchangeClazz()))
          .requestInterceptor(RestClientLoggingInterceptor(group.exchangeClazz()))
      }
    }
  }

  private fun HttpServiceGroup.exchangeClazz() = this.httpServiceTypes().first()
}

private class RestClientErrorHandler(clazz: Class<*>) : RestClient.ResponseSpec.ErrorHandler {
  private val log = LoggerFactory.getLogger(clazz)

  override fun handle(request: HttpRequest, response: ClientHttpResponse) {
    log.warn("code: {}, message: {}", response.statusCode.value(), response.statusText)
  }
}

private class RestClientLoggingInterceptor(clazz: Class<*>) : ClientHttpRequestInterceptor {
  private val log = LoggerFactory.getLogger(clazz)

  override fun intercept(
    request: HttpRequest,
    body: ByteArray,
    execution: ClientHttpRequestExecution
  ): ClientHttpResponse {
    log.info("request: [{}] {}", request.method, request.uri)
    return execution.execute(request, body)
  }
}
