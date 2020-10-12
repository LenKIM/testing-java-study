실행하기 전에 딱 한번 실행되는 것.

## 1. JUnit5 소개

https://junit.org/junit5/docs/current/user-guide/#overview-getting-started



## 2. JUnit5 시작하기

***기본 어노테이션***

@Test

@BeforeAll / @AfterAll

@BeforeEach / @AfterEach

@Disabled

```java
@Test
@Disabled
void create() {
  System.out.println("create1");
  Study study = new Study();
  assertNotNull(study);
}

@Test
void create2() {
  System.out.println("create2");
  Study study = new Study();
  assertNotNull(study);
}

@BeforeAll
static void beforeAll() {
  System.out.println("before all");
}

@AfterAll
static void afterAll() {
  System.out.println("after all");
}

@BeforeEach
void setUp() {
  System.out.println("beforeEach");
}

@AfterEach
void tearDown() {
  System.out.println("afterEach");
}

// result
before all
beforeEach
create1
afterEach
beforeEach
create2
afterEach
after all
```



## 3. 테스트 이름 표기하기

```java
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StudyTest {

  @Test
  @DisplayName("스터디 만들기")
  void create_study_1() {
    System.out.println("create1");
    Study study = new Study();
    assertNotNull(study);
  }
	...
}
```

![image-20201012143452989](https://tva1.sinaimg.cn/large/007S8ZIlgy1gjmhqfbbk0j30lg0feab2.jpg)

**@DisplayNameGeneration**

- Method와 Class 레퍼런스를 사용해서 테스트 이름을 표기하는 방법 설정.
- 기본 구현체로 ReplaceUnderscores 제공

**@DisplayName**

- 어떤 테스트인지 테스트 이름을 보다 쉽게 표현할 수 있는 방법을 제공하는 애노테이션.

- @DisplayNameGeneration 보다 우선 순위가 높다.



## 4. Assertion



**org.junit.jupiter.api.Assertions.***

![image-20201012150806748](https://tva1.sinaimg.cn/large/007S8ZIlgy1gjmip0acuhj310m0i2ad8.jpg)



```java
@Test
@DisplayName("스터디 만들기1")
void create_study_1() {
  IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
  String message = exception.getMessage();
  assertEquals("limit은 0보다 커야 한다.", message);
}

@Test
@DisplayName("스터디 만들기2")
void create_study_2() {
  System.out.println("create2");
  Study study = new Study(10);

  assertAll(
    () -> assertNotNull(study),
    () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값은 DRAFT이다."),
    () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은  0보다 커야 한다.")
  );

  assertNotNull(study);
  assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값은 DRAFT이다.");
  assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은  0보다 커야 한다.");
}

@Test
@DisplayName("스터디 만들기3")
void create_study_3() {
  assertTimeout(Duration.ofMillis(100), () ->{
    new Study(10);
    Thread.sleep(50);
  }
               );

}

```

## 5. 조건에 따라 테스트 실행

```java
@Test
@DisplayName("스터디 만들기5-enableOnOs")
@EnabledOnOs({OS.MAC, OS.LINUX})
void create_study_5() {
  String test_env = System.getenv("TEST_ENV");
  System.out.println(test_env);
  assumeTrue("LEN".equalsIgnoreCase(test_env));

  assumingThat("LEN".equalsIgnoreCase(test_env), () -> {
    System.out.println("len");
    Study actual = new Study(100);
    assertThat(actual.getLimit()).isGreaterThan(0);
  });
}

---
  
@EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "aa")
Environment variable [TEST_ENV] with value [LEN] does not match regular expression [aa]

```

org.junit.jupiter.api.Assumptions.*

- assumeTrue(조건)
- assumingThat(조건, 테스트)

@Enabled_ 와 @Disabled_

- OnOS
- OnJre
- IfSystemProperty
- IfEnvironmentVariable
- If





## 6. 태깅과 필터링

테스트 그룹을 만들고 원하는 테스트 그룹만 테스트를 실행할 수 있는 기능

```java
@Test
@DisplayName("스터디 만들기6-fast")
@Tag("fest")
void create_study_6() {
  System.out.println("fast");
}


@Test
@DisplayName("스터디 만들기5-slow")
@Tag("slow")
void create_study_7() {
  System.out.println("slow");
}
```

![image-20201012163040559](https://tva1.sinaimg.cn/large/007S8ZIlgy1gjml2xz2g9j317p0u0wsw.jpg)

@Tag

- 테스트 메소드에 태그를 추가할 수 있다.
- 하나의 테스트 메소드에 여러 태그를 사용할 수 있다.



## 7. 커스텀 태그

Junit5 애노테이션을 조합하여 커스텀 태그를 만들 수 있다.

메타에노테이션으로 사용할 수 있다.

```java
// festTest.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("fast")
public @interface FastTest {
}
```

위와같이 정의한 메타에노테이션이 있다면,

```java
@Test
@DisplayName("스터디 만들기6-fast")
@FastTest
void create_study_6() {
  System.out.println("fast");
}


@Test
@DisplayName("스터디 만들기5-slow")
@SlowTest
void create_study_7() {
  System.out.println("slow");
}

```

와 같이 적용시킬 수 있다.



## 8. 반복 테스트하기

```java
@RepeatedTest(10)
@DisplayName("스터디 만들기5-repeatTest")
void create_study_8(RepetitionInfo repetitionInfo) {
  System.out.println(repetitionInfo.getCurrentRepetition() + " / " + repetitionInfo.getTotalRepetitions());
  System.out.println("test");
}
```

![image-20201012164316721](https://tva1.sinaimg.cn/large/007S8ZIlgy1gjmlg03kccj31950u043d.jpg)

@RepeatedTest

- 반복횟수와 반복 테스트 이름을 설정할 수 있다.
  - {displayNames}
  - {currentRepetition}
  - {totalRepetitions}
- RepetitionInfo 타입의 인자를 받을 수 있다.



```java
@DisplayName("스터디 만들기9-ParameterizedTest")
@ParameterizedTest(name = "{index} {displayName} message={0}")
@ValueSource(strings = {"A", "B", "C", "D"})
void create_study_9(String messages) {
  System.out.println(messages);
}
```

![image-20201012165115288](https://tva1.sinaimg.cn/large/007S8ZIlgy1gjmloaev90j31ax0u0788.jpg)