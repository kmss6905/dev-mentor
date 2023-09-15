package site.devmentor.dto.comment;

import lombok.Builder;
import site.devmentor.domain.comment.Comment;
import site.devmentor.util.DateUtils;

public class CommentCreateResponse {

  private long commentId;
  private String comment;
  private String createdAt;

  @Builder
  public CommentCreateResponse(final long commentId, final String comment, final String createdAt) {
    this.commentId = commentId;
    this.comment = comment;
    this.createdAt = createdAt;
  }

  public static CommentCreateResponse from(Comment comment) {
    return CommentCreateResponse.builder()
            .commentId(comment.getId())
            .comment(comment.getContent())
            .createdAt(DateUtils.toStandardDateFormat(comment.getCreatedAt()))
            .build();
  }
}
