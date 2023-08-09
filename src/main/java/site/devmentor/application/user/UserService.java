package site.devmentor.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.application.PasswordService;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.user.UserCreateRequest;
import site.devmentor.exception.user.DuplicateEmailException;
import site.devmentor.exception.user.DuplicateUserIdException;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordService passwordService;

  public UserService(UserRepository userRepository, PasswordService passwordService) {
    this.userRepository = userRepository;
    this.passwordService = passwordService;
  }


  public void join(UserCreateRequest request) {
    verifyUniqueUserIdAndEmail(request);
    String encryptPwd = passwordService.encrpyt(request.getPassword());
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
}
