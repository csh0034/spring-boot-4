package com.ask.springboot4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.expression.spel.standard.SpelExpressionParser

@SpringBootTest
class SpelElvisTest {
  @Test
  fun elvis() {
    val parser = SpelExpressionParser()
    val name = parser.parseExpression("name ?: 'Unknown'").getValue(Inventor(), String::class.java)
    assertThat(name).isEqualTo("Unknown")
  }
}

private data class Inventor(val name: String? = null)
