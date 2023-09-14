package site.devmentor.utill;

import site.devmentor.domain.post.Post;

public class PostFactory {
  public static Post post(long userPid) {
    return Post.builder()
            .title("title")
            .content("content")
            .userPid(userPid)
            .build();
  }
}
