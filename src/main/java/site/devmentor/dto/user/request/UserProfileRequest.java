package site.devmentor.dto.user.request;

import jakarta.validation.constraints.NotEmpty;

public record UserProfileRequest(
        @NotEmpty(message = "content 는 필수 입력사항 입니다.")
        String content
) {
}