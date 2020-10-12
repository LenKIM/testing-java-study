package com.example.testingjavastudy;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StudyTest {

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

  @Test
  @DisplayName("스터디 만들기4-assumeTrue")
  void create_study_4() {
    String test_env = System.getenv("TEST_ENV");
    System.out.println(test_env);
    assumeTrue("LEN".equalsIgnoreCase(test_env));

    assumingThat("LEN".equalsIgnoreCase(test_env), () -> {
      System.out.println("len");
      Study actual = new Study(100);
      assertThat(actual.getLimit()).isGreaterThan(0);
    });
  }

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


  @DisplayName("스터디 만들기8-repeatTest")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition} / {totalRepetitions}")
  void create_study_8(RepetitionInfo repetitionInfo) {
    System.out.println(repetitionInfo.getCurrentRepetition() + " / " + repetitionInfo.getTotalRepetitions());
    System.out.println("test");
  }

  @DisplayName("스터디 만들기9-ParameterizedTest")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(strings = {"A", "B", "C", "D"})
  void create_study_9(String messages) {
    System.out.println(messages);
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
}