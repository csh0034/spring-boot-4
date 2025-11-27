package com.ask.springboot4.web

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * 7.0.1 기준 baseline 기능 정상동작안함
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class VersioningControllerTest {
  @Autowired
  private lateinit var mockMvc: MockMvc

  @ParameterizedTest
  @CsvSource(
    value = [
      "null, no version",
      "1.1, 1.1",
      "1.2, 1.2+",
      "1.3, 1.2+",
      "1.4, 1.4",
    ]
  )
  fun versioning(version: String, result: String) {
    mockMvc.get("/versioning") {
      accept = MediaType.APPLICATION_JSON
      if (version != "null") queryParam("version", version)
    }.andExpectAll {
      status { isOk() }
      jsonPath("$.version") { value(result) }
    }
  }
}
