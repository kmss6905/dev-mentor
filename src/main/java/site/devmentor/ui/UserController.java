package site.devmentor.ui;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.user.UserService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.Response;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.user.request.UserCreateRequest;
import site.devmentor.dto.user.request.UserProfileRequest;
import site.devmentor.dto.user.response.UserProfileResponse;
import site.devmentor.exception.user.DuplicateEmailException;
import site.devmentor.exception.user.DuplicateUserIdException;


@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Response> signUp(@Valid @RequestBody UserCreateRequest userCreateRequest) {
    userService.join(userCreateRequest);
    return ResponseUtil.ok();
  }

  @GetMapping("/{id}/exists")
  public ResponseEntity<Response> checkUniqueId(
          @PathVariable
          @NotEmpty(message = "아이디를 입력하기 바랍니다.")
          @NotBlank(message = "잘못된 아이디 양식입니다.")
          @Size(min = 5, max = 15, message = "아이디는 최소 5자리 이상 15자리 이하 이어야 합니다.")
          String id) {
    boolean isExistUserId = userService.isExistsId(id);
    if (isExistUserId){
      throw new DuplicateUserIdException(id);
    }
    return ResponseUtil.ok();
  }

  @GetMapping("/email/{email}/exists")
  public ResponseEntity<Response> checkUniqueEmail(
          @PathVariable
          @NotBlank(message = "이메일을 입력바랍니다.")
          @Email(message = "잘못된 이메일 양식 입니다.")
          String email) {
    boolean isExistEmail = userService.isExistsEmail(email);
    if (isExistEmail) {
      throw new DuplicateEmailException(email);
    }
    return ResponseUtil.ok();
  }

  @PatchMapping("/profile")
  public ResponseEntity<UserProfileResponse> updateProfile(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody UserProfileRequest profileRequest) {
    UserProfileResponse userProfileResponse =  userService.updateProfile(authUser, profileRequest);
    return ResponseEntity.ok(userProfileResponse);
  }

  @DeleteMapping("/profile")
  public ResponseEntity<Response> deleteProfile(
          @LoginUser AuthenticatedUser authUser
  ) {
    userService.deleteProfile(authUser);
    return ResponseUtil.ok();
  }
}
