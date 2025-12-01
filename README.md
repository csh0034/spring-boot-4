# Spring Boot 4

- https://spring.io/blog/2025/11/20/spring-boot-4-0-0-available-now
  - https://github.com/spring-projects/spring-boot/releases/tag/v4.0.0
  - 25.11.21

## kotlin 관련

- spring 7 은 jdk25 를 권하지만 kotlin 2.2 기준 java25 미지원, 2.3.0-Beta2 부터 지원함
- 2.3.0: Planned for December 2025 – January 2026

## Spring 7 관련

- https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-7.0-Release-Notes
- JDK 17을 최소 요구 버전으로 유지하지만, 최신 LTS 버전인 JDK 25 사용을 권장

### spring jcl 제거

- apache commons logging 1.3.0 사용

### 프로그래밍 방식 Bean 등록 (BeanRegistrar)

- https://docs.spring.io/spring-framework/reference/core/beans/java/programmatic-bean-registration.html
- 특정조건의경우 타입세이프 하지 않은 ConditionalOnProperty 보다 나을것 같음
  - @Bean 내부에서 @ConfigurationProperties 대상을 접근하여 null 을 반환하는것도 type safe 처리 가능

### CORS Pre-flight 동작 변경

- CORS 설정이 비어 있더라도 pre-flight 요청을 거부하지 않음.

### SpringExtension 테스트 컨텍스트 범위 변경

- https://github.com/spring-projects/spring-framework/issues/35697
  - 깃헙 이슈봐도 뭔소린지 모르겠음..
- JUnit Jupiter의 SpringExtension이 테스트 메서드 단위 컨텍스트로 변경
- @Nested 클래스 계층에서 DI 동작을 일관되게 해주지만, 기존 TestExecutionListener 커스터마이징 코드가 깨질 수 있음.

### Resilience features

- https://spring.io/blog/2025/09/09/core-spring-resilience-features
- spring retry 프로젝트를 spring-core 모듈에 통합
- RetryTemplate, @Retryable, @ConcurrencyLimit

### api versioning

- 1.0+ 형식의 베이스라인 기능이 현재 잘못 동작함
- AbstractHandlerMapping.getHandler
  - initApiVersion
    - DefaultApiVersionStrategy 처리시에 베이스라인 고려를 하지 않음
- RequestMappingInfoHandlerMapping.handleMatch
  - VersionRequestCondition.handleMatch 베이스라인 처리되어있음
- 실제 initApiVersion 잘못 처리 되는부분은 우선순위가 높은 RouterFunctionMapping

### http interface 자동구성

- @ImportHttpServices 통해서 가능
- group 이라는 개념이 추가됨
  - 설정하지 않을 경우 default 그룹
- RestClientHttpServiceGroupConfigurer 통해서 커스터마이징

## spring boot 4.0 관련

- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- Java 17 이상 필요 (권장: 최신 LTS), Kotlin v2.2 이상, Gradle 9

### AOT를 통한 향상된 네이티브 컴파일

- 네이티브 이미지 생성 개선

### 모듈 의존성 변경

- Spring Boot 4.0은 모듈화되어 이전보다 작은 모듈 단위로 제공
- 테스트 인프라도 모듈화되어 있어 별도 의존성 확인 필요

### 스타터 정리

- 각 기술마다 전용 starter 제공
- 세분화 되었으며 변경된 이름이 있으므로 세팅시 확인 필요

### fully executable jar 제거

- init.d 를 통한 실행도 함께 제거

## Hibernate 7

- https://docs.hibernate.org/orm/7.0/migration-guide/

### StatelessSession 동작 변경

- 1차캐시, 변경감지, lazy loading 등이 동작하지 않아 성능상 이점이 있음
- 개별 객체를 각각 트래킹하지 않아도 되는 가볍고 빠른 벌크 처리를 위해 고안된 디자인
- 기본 동작 변경, L2 cache default enabled 로 변경
- jdbc.batch_size 영향 없이 setJdbcBatchSize 사용

### DDL 변경

- timestamp, oracle/sql server precision 변경
- array mapping, mysql/mariadb json_array 로 변경
  - 기존방식 유지: hibernate.type.preferred_array_jdbc_type=VARBINARY

## jackson3 관련

- https://spring.io/blog/2025/10/07/introducing-jackson-3-support-in-spring
- https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0
- package (and dependency groupID) change from com.fasterxml.jackson to tools.jackson
  - jackson-annotations는 호환성을 위해 그대로 유지, @JsonValue 등
- switch from a mutable ObjectMapper in Jackson 2 to an immutable JsonMapper in Jackson 3.
  - JsonMapper 가 ObjectMapper 를 상속 하였음
  - JsonMapperBuilderCustomizer 등과 같이 용어가 변경되었으므로 코드에서도
- 추상화된 타입(ObjectMapper) 보단 JsonMapper 로 변경하는게 좋을것 같음

### 업그레이드 시 권장 순서

1. Jackson 3로 마이그레이션
2. 필요 시 spring.jackson.use-jackson2-defaults=true 설정 후 마이그레이션
   - jackson2 와 비슷한 기본값 사용 (호환성 유지를 위함)
3. 임시로 Jackson 2 사용 → 단계적 Jackson 3 이동
   - jackson3 의존성 제외 하고 spring-boot-jackson2 사용하면됨

### MappingJacksonValue 제거

- 사용해야 할 경우 SmartHttpMessageConverter 의 hint 로 제공
- this.restClient.post().uri("http://localhost:8080/create").hint(JsonView.class.getName(), Summary.class) 로 제공

### Module 관련

- 하단 기능 기본 탑재됨 (jackson-databind 로 머지되었음)
  - jackson-module-java8
  - parameter-names
  - datatype-jsr310(JavaTimeModule)

### 기본값 변경

- MapperBuilder.configureForJackson2 참고
- 자동설정의 경우 spring.jackson.use-jackson2-defaults=true 를 통해 호출됨
  - 추가로 spring boot 에서는 하단 2개 비활성화하였음
  - .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

### kotlin module 의존성 변경

- https://github.com/FasterXML/jackson-module-kotlin
- springio 자동생성시 com.fasterxml 로 코틀린 모듈 생성됨  
  따라서 tools.jackson.module 로 변경해야함

## jasypt-spring-boot

- https://github.com/ulisesbocchio/jasypt-spring-boot
- repository 가 관리되고 있지 않음
- 최근 커밋이 3년전

## version

### spring cloud 2025.1

- 25.11.24

### spring data jpa 4.0.0

- https://github.com/spring-projects/spring-data-jpa/releases/tag/4.0.0

### hibernate 7.1.8

- https://github.com/hibernate/hibernate-orm/releases/tag/7.1.8

### spring 7.0.1

- https://github.com/spring-projects/spring-framework/releases/tag/v7.0.1

### spring security 7.0.0

- https://github.com/spring-projects/spring-security/releases/tag/7.0.0

### spring kafka 4.0.0

- https://github.com/spring-projects/spring-kafka/releases/tag/v4.0.0

### tomcat 11.0.14

- https://tomcat.apache.org/tomcat-11.0-doc/changelog.html

### mysql connector 9.5.0

- https://dev.mysql.com/doc/relnotes/connector-j/en/news-9-5-0.html
