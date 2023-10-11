package site.devmentor.dto.mentor.schedule;

import site.devmentor.domain.mentor.schedule.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponse(long scheduleId, LocalDateTime createdAt) {
  public static ScheduleResponse of(Schedule schedule) {
    return new ScheduleResponse(
        schedule.getId(),
        schedule.getCreatedAt()
    );
  }
}
