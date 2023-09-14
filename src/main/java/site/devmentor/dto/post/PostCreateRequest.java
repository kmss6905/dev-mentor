package site.devmentor.dto.post;

import jakarta.validation.constraints.NotNull;


public record PostCreateRequest(
        @NotNull(message = "[title]은 Null 이여서는 안됩니다.") String title,
        @NotNull(message = "[content] 은 Null 이여서는 안됩니다.") String content) {
}
