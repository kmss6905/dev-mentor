package site.devmentor.dto.mentor.schedule.detail;

import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailStatus;

import java.time.LocalDateTime;

public record ScheduleDetailStatusUpdateResponse(
    ScheduleDetailStatus status,
    LocalDateTime updateAt
) {
    public static ScheduleDetailStatusUpdateResponse from(final ScheduleDetail detail) {
        return new ScheduleDetailStatusUpdateResponse(detail.getStatus(), detail.getUpdatedAt());
    }
}
