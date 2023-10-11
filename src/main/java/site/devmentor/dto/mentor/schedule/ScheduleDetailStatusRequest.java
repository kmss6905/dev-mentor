package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailStatus;

public record ScheduleDetailStatusRequest(
    @NotNull ScheduleDetailStatus status
) {
}
