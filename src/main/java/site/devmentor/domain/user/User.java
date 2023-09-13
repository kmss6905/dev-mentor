package site.devmentor.domain.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.user.vo.UserProfile;
import site.devmentor.dto.user.request.UserProfileRequest;

@Table(name = "USERS")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Column(nullable = false, name = "user_id")
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Enumerated
  private UserProfile profile;

  @Builder
  private User(final String userId, final String password, final String email){
    this.userId = userId;
    this.password = password;
    this.email = email;
    this.role = UserRole.BOTH;
  }

  public void updateProfile(UserProfileRequest userProfileRequest) {
    if (isProfileNotEmpty()) {
      this.profile.update(userProfileRequest.content());
      return;
    }
    this.profile = new UserProfile(userProfileRequest.content());
  }

  private boolean isProfileNotEmpty() {
    return this.profile != null;
  }

  public void deleteProfile() {
    if (isProfileNotEmpty()) {
      this.profile = null;
    }
  }
}
