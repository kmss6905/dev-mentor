package site.devmentor.application;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class BcryptPwdService implements PasswordService {

  @Override
  public String encrpyt(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean isMatches(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }
}
