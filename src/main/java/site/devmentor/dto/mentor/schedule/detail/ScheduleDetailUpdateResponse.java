package site.devmentor.dto.mentor.schedule.detail;

import site.devmentor.domain.mentor.schedule.ScheduleDetail;

import java.time.LocalDateTime;

public record ScheduleDetailUpdateResponse(
        long detailId,
        LocalDateTime updatedAt
) {

  public static ScheduleDetailUpdateResponse of(ScheduleDetail detail) {
    return new ScheduleDetailUpdateResponse(detail.getId(), detail.getUpdatedAt());
  }
}
