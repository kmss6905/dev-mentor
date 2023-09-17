package site.devmentor.dto.comment;

import lombok.Builder;
import site.devmentor.domain.comment.Comment;
import site.devmentor.util.DateUtils;

public record CommentCreateResponse(long commentId, String comment, String createdAt) {

  @Builder
  public CommentCreateResponse {
  }

  public static CommentCreateResponse from(Comment comment) {
    return CommentCreateResponse.builder()
            .commentId(comment.getId())
            .comment(comment.getContent())
            .createdAt(DateUtils.toStandardDateFormat(comment.getCreatedAt()))
            .build();
  }
}
