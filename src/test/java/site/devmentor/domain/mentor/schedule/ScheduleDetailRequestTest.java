package site.devmentor.domain.mentor.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailTime;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMenteeMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMentorMemo;
import site.devmentor.exception.schedule.ScheduleDetailContentValidateException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@DisplayName("Schedule Detail 도메인 테스트")
class ScheduleDetailRequestTest {

  @ParameterizedTest(name = "{index} ==> {2} [시작:[{0}] / 종료:[{1}]]")
  @CsvSource({
          "2023-01-01T01:01:00, 2023-01-01T01:01:00, 시간같음",
          "2023-01-01T01:01:00, 2023-01-01T01:00:00, 종료가`1시간`느림",
          "2023-01-01T01:01:01, 2023-01-01T01:01:00, 종료가`1분`느림",
          "2024-12-02T00:00:00, 2024-12-01T23:59:59, 종료가`1초`느림",
  })
  void 시작시간이_종료시간보다_뒤에있거나_같다면_예외를_던진다(LocalDateTime startTime, LocalDateTime endTime, String desc) {

    assertThrowsExactly(IllegalArgumentException.class ,() -> ScheduleDetail.builder()
            .title("detail_title")
            .time(ScheduleDetailTime.of(startTime, endTime))
            .memo(
                    ScheduleDetailMemo.create(
                            ScheduleMentorMemo.from("content"),
                            ScheduleMenteeMemo.from("content")
                    )
            )
            .build());
  }

  @ParameterizedTest(name = "{index} ==> {0}")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void 멘티메모가_없거나_빈_문자열이면_예외를_던진다(String menteeMemoValue) {
    LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 1, 0);
    LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 2, 0);

    assertThrowsExactly(ScheduleDetailContentValidateException.class,() -> {
      ScheduleMenteeMemo menteeMemo = menteeMemoValue != null ? ScheduleMenteeMemo.from(menteeMemoValue) : null;
      ScheduleDetail.builder()
              .title("detail_title")
              .time(ScheduleDetailTime.of(startTime, endTime))
              .memo(ScheduleDetailMemo.create(
                      ScheduleMentorMemo.from("memo"),
                      menteeMemo
              ))
              .build();
    });
  }


  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void 제목이_없거나_빈_문자열이면_예외를_던진다(String titleValue) {
    LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 1, 0);
    LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 2, 0);

    assertThrowsExactly(ScheduleDetailContentValidateException.class,
            () -> ScheduleDetail.builder()
                    .title(titleValue)
                    .time(ScheduleDetailTime.of(startTime, endTime))
                    .memo(ScheduleDetailMemo.create(
                            ScheduleMentorMemo.from("memo"),
                            ScheduleMenteeMemo.from("memo")
                    ))
                    .build());
  }


  @Test
  void 시간이_없으면_예외를_던진다() {
    assertThrowsExactly(IllegalArgumentException.class ,() -> ScheduleDetail.builder()
            .title("detail_title")
            .time(ScheduleDetailTime.of(null, null))
            .memo(ScheduleDetailMemo.create(
                    ScheduleMentorMemo.from("memo"),
                    ScheduleMenteeMemo.from("memo")
            ))
            .build());
  }
}