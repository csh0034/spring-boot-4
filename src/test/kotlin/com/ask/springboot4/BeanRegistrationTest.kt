package com.ask.springboot4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(MyBeanRegistrar::class)
class BeanRegistrationTest {
  @Autowired
  @Qualifier("foo1")
  private lateinit var foo1: Foo

  @Autowired
  @Qualifier("foo2")
  private lateinit var foo2: Foo

  @Autowired
  private lateinit var bar1: Bar

  @Autowired
  private lateinit var bar2: Bar

  @Test
  fun bean() {
    assertThat(foo1.name).isEqualTo("hi")
    assertThat(foo2.name).isEqualTo("Hello World!")
    assertThat(bar1).isNotSameAs(bar2)
    assertThat(bar1.foo).isSameAs(foo1)
  }
}

class MyBeanRegistrar : BeanRegistrarDsl({
  registerBean<Foo>(name = "foo1")

  registerBean(
    name = "bar",
    prototype = true,
    lazyInit = true,
    description = "Custom description"
  ) {
    Bar(bean<Foo>("foo1"))
  }

  profile("test") {
    registerBean(
      name = "foo2",
    ) { Foo("Hello World!") }
  }
})

private open class Foo(
  val name: String = "hi"
)

private open class Bar(
  val foo: Foo
)
