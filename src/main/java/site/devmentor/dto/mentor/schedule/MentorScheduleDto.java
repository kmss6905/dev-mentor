package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


public record MentorScheduleDto(
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
}
