package site.devmentor.utill;

import site.devmentor.domain.user.User;

public class UserFactory {
  public static User user() {
    return User.builder()
            .userId("user123")
            .email("user1@gmail.io")
            .password("user1password!")
            .build();
  }
}
