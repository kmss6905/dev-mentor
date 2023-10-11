package site.devmentor.application.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.user.request.UserCreateRequest;
import site.devmentor.dto.user.request.UserProfileRequest;
import site.devmentor.dto.user.response.UserProfileResponse;
import site.devmentor.exception.user.DuplicateEmailException;
import site.devmentor.exception.user.DuplicateUserIdException;
import site.devmentor.exception.user.UserNotFoundException;

@Slf4j
@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }


  public void join(UserCreateRequest request) {
    verifyUniqueUserIdAndEmail(request);
    String encryptPwd = passwordEncoder.encode(request.getPassword());
    User encryptedUser = request.toEntity(encryptPwd);
    userRepository.save(encryptedUser);
  }

  @Transactional(readOnly = true)
  public void verifyUniqueUserIdAndEmail(UserCreateRequest request) {
    validateUniqueUserId(request.getUserId());
    validateUniqueEmail(request.getEmail());
  }

  @Transactional(readOnly = true)
  public boolean isExistsId(String userId) {
    return userRepository.existsUserByUserId(userId);
  }

  @Transactional(readOnly = true)
  public boolean isExistsEmail(String email) {
    return userRepository.existsUserByEmail(email);
  }

  private void validateUniqueUserId(String userId) {
    boolean existed = userRepository.existsUserByUserId(userId);
    if (existed) {
      throw new DuplicateUserIdException(userId);
    }
  }

  private void validateUniqueEmail(String email) {
    boolean existed = userRepository.existsUserByEmail(email);
    if (existed) {
      throw new DuplicateEmailException(email);
    }
  }

  public UserProfileResponse updateProfile(AppUser authUser, UserProfileRequest profileRequest) {
    User user = findUser(authUser);
    user.updateProfile(profileRequest);
    return UserProfileResponse.from(user);
  }

  public void deleteProfile(AppUser authUser) {
    User user = findUser(authUser);
    user.deleteProfile();
  }


  private User findUser(AppUser authUser) {
    return userRepository.findById(authUser.pid())
            .orElseThrow(() -> new UserNotFoundException(String.valueOf(authUser.pid())));
  }
}
