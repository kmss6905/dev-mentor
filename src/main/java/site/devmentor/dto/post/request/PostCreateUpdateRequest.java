package site.devmentor.dto.post.request;

import jakarta.validation.constraints.NotNull;


public record PostCreateUpdateRequest(
        @NotNull(message = "[title]은 Null 이여서는 안됩니다.") String title,
        @NotNull(message = "[content] 은 Null 이여서는 안됩니다.") String content) {
}
