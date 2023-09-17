package site.devmentor.dto.post.response;


import lombok.Builder;
import lombok.Getter;
import site.devmentor.domain.post.Post;
import site.devmentor.util.DateUtils;

@Getter
public class PostUpdateResponse {

  private final long postId;
  private final String createdAt;
  private final String updatedAt;

  @Builder
  private PostUpdateResponse(final long postId, final String createdAt, final String updatedAt) {
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
