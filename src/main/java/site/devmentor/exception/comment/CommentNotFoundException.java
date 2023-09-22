package site.devmentor.exception.comment;

import site.devmentor.exception.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {
  public CommentNotFoundException(String id) {
    super("댓글을 찾을 수 없습니다. 댓글 번호 : " + id);
  }
}
