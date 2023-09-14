package site.devmentor.dto.post.response;


public record PostUpdateResponse (
  long postId,
  String createdAt,
  String updatedAt
){}
