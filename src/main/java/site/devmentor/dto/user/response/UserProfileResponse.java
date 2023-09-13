package site.devmentor.dto.user.response;

import lombok.Builder;
import site.devmentor.domain.user.User;
import site.devmentor.util.DateUtils;

public record UserProfileResponse(long id, String userId, String content, String createdAt, String updatedAt) {
  public static UserProfileResponse from(User user) {
    return UserProfileResponse.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .content(user.getProfile().getContent())
            .createdAt(DateUtils.toStandardDateFormat(user.getCreatedAt()))
            .updatedAt(DateUtils.toStandardDateFormat(user.getUpdatedAt()))
            .build();
  }

  @Builder
  public UserProfileResponse {
  }

}
