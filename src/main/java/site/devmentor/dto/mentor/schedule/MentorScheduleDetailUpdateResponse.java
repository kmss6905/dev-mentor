package site.devmentor.dto.mentor.schedule;

import site.devmentor.domain.mentor.schedule.ScheduleDetail;

import java.time.LocalDateTime;

public record MentorScheduleDetailUpdateResponse(
        long detailId,
        LocalDateTime updatedAt
) {

  public static MentorScheduleDetailUpdateResponse of(ScheduleDetail detail) {
    return new MentorScheduleDetailUpdateResponse(detail.getId(), detail.getUpdatedAt());
  }
}
