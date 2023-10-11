package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailTime;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMenteeMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMentorMemo;

import java.time.LocalDateTime;


public record ScheduleDetailRequest(
    @NotNull(message = "[title] 는 필수입니다.")
    String title,
    @NotNull(message = "[startDate] 은 필수 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate,
    @NotNull(message = "[endDate] 은 필수 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate,
    @NotNull(message = "[menteeMemo] 는 필수입니다.")
    String menteeMemo,
    @NotNull(message = "[mentorMemo] 는 필수입니다.")
    String mentorMemo
) {

  public ScheduleDetail toDetailWithSchedule(Schedule schedule) {
    return ScheduleDetail.builder()
        .memo(ScheduleDetailMemo.createWithMenteeMentorMemo(
            ScheduleMenteeMemo.from(menteeMemo),
            ScheduleMentorMemo.from(mentorMemo)))
        .schedule(schedule)
        .title(title)
        .time(ScheduleDetailTime.of(startDate, endDate))
        .build();
  }
}
