package site.devmentor.dto.user.response;

import lombok.Builder;
import site.devmentor.domain.user.User;

public record UserProfileResponse(long id, String userId, String content, String createdAt, String updatedAt) {
  public static UserProfileResponse from(User user) {
    return UserProfileResponse.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .content(user.getProfile().getContent())
            .createdAt(user.getCreatedAt().toString())
            .updatedAt(user.getUpdatedAt().toString())
            .build();
  }

  @Builder
  public UserProfileResponse {
  }

}
