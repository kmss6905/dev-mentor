package site.devmentor.exception.user;


import site.devmentor.exception.DuplicateException;

public class DuplicateEmailException extends DuplicateException {

  public DuplicateEmailException(String email) {
    super(String.format("이메일 '%s'는 이미 사용 중입니다.", email));
  }
}
