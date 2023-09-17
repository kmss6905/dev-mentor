package site.devmentor.utill;

import site.devmentor.domain.user.User;

public class UserFactory {
  public static User user(String userId) {
    return User.builder()
            .userId(userId)
            .email(userId + "@gmail.io")
            .password("user1Password")
            .build();
  }
}
