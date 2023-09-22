package site.devmentor.dto.mentor;

import jakarta.validation.constraints.NotNull;

public record MentorRequestDto(
        long mentorUserId,
        @NotNull(message = "[memo]입력은 필수입니다.") String memo
){}