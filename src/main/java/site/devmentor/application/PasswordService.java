package site.devmentor.application;

/**
 * 현 시점에서 어떤 암호화 방법을 적용하여 비밀번호를 암호화 할 지 정하지 않았다.
 * 추후 구체적인 암호화 방식이 바뀔 경우를 대비하여 인터페이스에 의존하도록 하고 구현체만 바꾸도록 한다.
 */

public interface PasswordService {
  String encrpyt(String pwd);

  boolean isMatches(String pwd, String hashedPwd);
}
