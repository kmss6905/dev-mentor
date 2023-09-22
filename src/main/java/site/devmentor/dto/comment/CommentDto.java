package site.devmentor.dto.comment;

import jakarta.validation.constraints.NotNull;

public record CommentDto(
        @NotNull(message = "[comment] 는 null이 될 수 없습니다.")
        String comment
) {}
