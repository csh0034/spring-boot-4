package com.ask.springboot4

import com.fasterxml.jackson.annotation.JsonValue
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.exc.MismatchedInputException
import tools.jackson.module.kotlin.jsonMapper
import tools.jackson.module.kotlin.kotlinModule
import tools.jackson.module.kotlin.readValue
import java.time.Instant

/**
 * MapperBuilder.configureForJackson2 참고하면 변경된 부분 알 수 있음
 */
class JsonMapperTest {
  val jsonMapper = jsonMapper {
    addModule(kotlinModule())
//    configureForJackson2()

    // jackson3 에서 default 값 변경된 옵션
    enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
    enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
    disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
  }

  @Test
  fun `json 에 key 가 잘못 입력되어있을 경우 예외 발생`() {
    assertThatThrownBy {
      jsonMapper.readValue("""{"invalid": "chat"}""", NameRequest::class.java)
    }.isInstanceOf(MismatchedInputException::class.java)
  }

  @Test
  fun `boolean 경우 json 에 프로퍼티 key 가 없으면 예외 발생함`() {
    assertThatNoException().isThrownBy {
      val request = jsonMapper.readValue("""{"name": "chat"}""", SampleRequest::class.java)

      // Boolean 의 경우엔 초기값 할당안해도 false 로 처리됨.
      assertThat(request.deleted).isFalse()
    }
  }

  @Test
  fun `enum의 경우 json 에 프로퍼티 key 가 없더라도 초기값이 할당되어 있으면 예외 발생안함`() {
    assertThatNoException().isThrownBy {
      jsonMapper.readValue<SampleRequest>("""{"name": "chat"}""")
    }
  }

  @Test
  fun `enum의 경우 json에 프로퍼티에 null이 할당되어 있는 경우 예외 발생`() {
    assertThatThrownBy {
      jsonMapper.readValue<SampleRequest>("""{"name": "chat", "sampleEnum": null}""")
    }.isInstanceOf(MismatchedInputException::class.java)
  }

  @Test
  fun `enum의 경우 nullable json에 프로퍼티에 null이 할당되어 있는 경우 초기값 셋팅 안됨`() {
    assertThatNoException().isThrownBy {
      val request = jsonMapper.readValue<SampleRequest>("""{"name": "chat", "nullableSampleEnum": null}""")
      assertThat(request.nullableSampleEnum).isNull()
    }
  }

  @Test
  fun `@JsonValue 사용시 해당 값으로 enum 이 직렬화됨`() {
    val map = mapOf("a" to SampleValueEnum.TEST_A)
    val json = jsonMapper.writeValueAsString(map)
    assertThat(json).isEqualTo("""{"a":"test_a"}""")
  }

  @Test
  fun `value class 직렬화 및 역직렬화`() {
    val input = mapOf(Id(1) to "one", Id(2) to "two")
    val serialized = jsonMapper.writeValueAsString(input)
    val deserialized = jsonMapper.readValue<Map<Id, String>>(serialized)
    assertThat(deserialized).isEqualTo(input)
  }

  @Test
  fun `Instant - Long 직렬화 및 역직렬화`() {
    val input = mapOf("time" to Instant.parse("2099-01-01T00:00:00Z"))
    val serialized = jsonMapper.writeValueAsString(input)
    val deserialized = jsonMapper.readValue<Map<String, Instant>>(serialized)

    assertThat(serialized).isEqualTo("""{"time":4070908800000}""")
    assertThat(deserialized).isEqualTo(input)
  }
}

private data class NameRequest(
  val name: String?,
)

private data class SampleRequest(
  val name: String,
  val deleted: Boolean,
  val ids: List<String> = emptyList(),
  val sampleEnum: SampleEnum = SampleEnum.TEST_1,
  val nullableSampleEnum: SampleEnum? = SampleEnum.TEST_2,
)

private enum class SampleEnum {
  TEST_1, TEST_2
}

private enum class SampleValueEnum {
  TEST_A, TEST_B;

  @JsonValue
  private fun lowercase() = this.name.lowercase()
}

@JvmInline
value class Id(val value: Int)
