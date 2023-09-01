package site.devmentor.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;

public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저 입니다."));
    return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(), AuthorityUtils.NO_AUTHORITIES);
  }
}
