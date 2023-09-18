package site.devmentor.dto.comment;

import site.devmentor.domain.comment.Comment;
import site.devmentor.util.DateUtils;

public record CommentUpdateResponse(long commentId, String comment, String updatedAt) {
  public static CommentUpdateResponse from(Comment comment) {
    return new CommentUpdateResponse(comment.getId(), comment.getContent(), DateUtils.toStandardDateFormat(comment.getUpdatedAt()));
  }
}
