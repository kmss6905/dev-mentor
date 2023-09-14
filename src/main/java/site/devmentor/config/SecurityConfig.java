package site.devmentor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import site.devmentor.auth.CustomUserDetailService;
import site.devmentor.auth.filter.JsonLoginAuthenticationFilter;
import site.devmentor.auth.filter.JsonLoginAuthenticationSuccessHandler;
import site.devmentor.domain.user.UserRepository;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;

  public SecurityConfig(ObjectMapper objectMapper, UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.userRepository = userRepository;
  }

  @Bean
  public AbstractAuthenticationProcessingFilter jsonLoginAuthenticationFilter() {
    JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter = new JsonLoginAuthenticationFilter(objectMapper);
    jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
    jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
    return jsonLoginAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // disable default formLogin
    http
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);

    http
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(
                                    antMatcher(POST, "/api/login"),
                                    antMatcher(POST, "/api/user"),
                                    antMatcher(GET, "/api/user/*/exists"),
                                    antMatcher(GET, "/api/user/email/*/exists")
                            ).permitAll()
                            .anyRequest().authenticated());

    // add json login filter
    http
            .addFilterBefore(jsonLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    return new ProviderManager(daoAuthenticationProvider);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailService(userRepository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler jsonLoginSuccessHandler() {
    return new JsonLoginAuthenticationSuccessHandler(objectMapper);
  }
}
