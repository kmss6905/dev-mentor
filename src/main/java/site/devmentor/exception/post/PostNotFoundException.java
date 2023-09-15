package site.devmentor.exception.post;

import site.devmentor.exception.ResourceNotFoundException;

public class PostNotFoundException extends ResourceNotFoundException {


  public PostNotFoundException(String id) {
    super("게시글을 찾을 수 없습니다. 게시글 번호 : " + id);
  }
}
