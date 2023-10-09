package site.devmentor.dto.mentor.schedule;

import java.time.LocalDateTime;

public record MentorScheduleDetailCreateResponse(
        long scheduleId,
        long scheduleDetailId,
        LocalDateTime createAt
) {
}
