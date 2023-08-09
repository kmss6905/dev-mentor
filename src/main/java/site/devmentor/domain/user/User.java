package site.devmentor.domain.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devmentor.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends BaseEntity {

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Builder
  private User(final String userId, final String password, final String email){
    this.userId = userId;
    this.password = password;
    this.email = email;
    this.role = UserRole.BOTH;
  }
}
