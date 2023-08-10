package site.devmentor.exception.user;

import site.devmentor.exception.DuplicateException;

public class DuplicateUserIdException extends DuplicateException {

  public DuplicateUserIdException(String message) {
    super(String.format("아이디 '%s'는 이미 사용 중입니다.", message));
  }
}
