package site.devmentor.dto.post.response;


import lombok.Builder;
import site.devmentor.domain.post.Post;
import site.devmentor.util.DateUtils;

public class PostUpdateResponse {

  private long postId;
  private String createdAt;
  private String updatedAt;

  @Builder
  private PostUpdateResponse(final long postId, final String createdAt, String updatedAt) {
    this.postId = postId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static PostUpdateResponse from(Post post) {
    return PostUpdateResponse.builder()
            .postId(post.getId())
            .createdAt(DateUtils.toStandardDateFormat(post.getCreatedAt()))
            .updatedAt(DateUtils.toStandardDateFormat(post.getUpdatedAt()))
            .build();
  }
}
