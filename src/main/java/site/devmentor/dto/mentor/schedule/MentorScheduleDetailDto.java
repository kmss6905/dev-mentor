package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


public record MentorScheduleDetailDto(
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
}
