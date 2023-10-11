package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.user.User;

import java.time.LocalDateTime;


public record ScheduleRequest(
    @NotNull(message = "[requestId] 는 필수입니다.")
    Long requestId,

    @NotNull(message = "[title] 은 필수 입니다.")
    String title,

    @NotNull(message = "[startDate] 은 필수 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate,

    @NotNull(message = "[endDate] 은 필수 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate,

    @NotNull(message = "[memo] 은 필수 입니다.")
    String memo
) {
  public Schedule toSchedule(User mentor, User mentee, MentorRequest request) {
    return Schedule.builder()
        .title(title)
        .startTime(startDate)
        .endTime(endDate)
        .memo(memo)
        .request(request)
        .author(mentor)
        .mentee(mentee)
        .build();
  }
}
