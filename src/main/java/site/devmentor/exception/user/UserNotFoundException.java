package site.devmentor.exception.user;

import site.devmentor.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException(String id) {
    super("존재하지 않는 유저 입니다. 유저번호 : " + id);
  }
}
