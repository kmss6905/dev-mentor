package site.devmentor.dto.post.response;

import site.devmentor.domain.post.Post;

public class PostCreateResponse{
  private final long postId;

  private PostCreateResponse(long postId) {
    this.postId = postId;
  }

  public static PostCreateResponse from (Post post) {
    return new PostCreateResponse(post.getId());
  }

  public long getPostId() {
    return postId;
  }
}
