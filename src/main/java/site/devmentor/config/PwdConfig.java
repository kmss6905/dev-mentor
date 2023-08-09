package site.devmentor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.devmentor.application.BcryptPwdService;
import site.devmentor.application.PasswordService;

@Configuration
public class PwdConfig {

  @Bean
  public PasswordService passwordService(){
    return new BcryptPwdService();
  }
}
