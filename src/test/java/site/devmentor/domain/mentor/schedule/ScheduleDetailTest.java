package site.devmentor.domain.mentor.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailTime;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMenteeMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMentorMemo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ScheduleDetailTest {

  @Test
  void 스케줄세부사항의_시작시간은_종료시간보다_일러야한다() {
    LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 1, 0);
    LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 2, 0);
    System.out.println(startTime);
    System.out.println(endTime);
    System.out.println(startTime.isBefore(endTime));

    assertDoesNotThrow(
            () -> {
              ScheduleDetail.builder()
                      .title("detail_title")
                      .time(
                              ScheduleDetailTime.create(
                                      startTime,
                                      endTime
                              )
                      )
                      .memo(
                              ScheduleDetailMemo.create(
                                      ScheduleMentorMemo.from("content"),
                                      ScheduleMenteeMemo.from("content")
                              )
                      )
                      .build();
            }
    );
  }
}