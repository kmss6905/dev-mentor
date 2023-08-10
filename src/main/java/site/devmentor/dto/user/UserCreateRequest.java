package site.devmentor.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import site.devmentor.domain.user.User;

@Getter
public class UserCreateRequest {

  @NotBlank(message = "유저 id 는 필수입니다.")
  @Size(min = 5, max = 15, message = "아이디는 최소 5자리 이상 15자리 이하 이어야 합니다.")
  private String userId;

  @NotBlank(message = "password 는 필수입니다.")
  @Size(min = 10, max = 20, message = "비밀번호는 최소 10자리 이상 20자리 이하 이어야 합니다.")
  private String password;

  @NotBlank(message = "email 은 필수입니다.")
  @Email(message = "잘못된 이메일 양식 입니다.")
  private String email;

  private UserCreateRequest() {
  }
  public UserCreateRequest(final String userId, final String password, final String email) {
    this.userId = userId;
    this.password = password;
    this.email = email;
  }

  public User toEntity(String encryptedPwd) {
    return User.builder()
            .userId(this.userId)
            .password(encryptedPwd)
            .email(this.email)
            .build();
  }
}
