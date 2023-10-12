package site.devmentor.dto.mentor.schedule.detail;

import site.devmentor.domain.mentor.schedule.ScheduleDetail;

import java.time.LocalDateTime;

public record ScheduleDetailCreateResponse(
        long scheduleId,
        long scheduleDetailId,
        LocalDateTime createAt
) {
  public static ScheduleDetailCreateResponse of(long scheduleId, ScheduleDetail scheduleDetail) {
    return new ScheduleDetailCreateResponse(scheduleId, scheduleDetail.getId(), scheduleDetail.getCreatedAt());
  }
}
