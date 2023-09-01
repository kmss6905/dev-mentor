package site.devmentor.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import site.devmentor.auth.LoginDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final static String HTTP_METHOD = "POST";
  private final static String DEFAULT_CONTENT_TYPE = "application/json";
  private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/login";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
          new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); //=>   /login 의 요청에, POST로 온 요청에 매칭된다.

  private final ObjectMapper objectMapper;

  public JsonLoginAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    if (!request.getMethod().equals(HTTP_METHOD) ||
            !request.getContentType().equals(DEFAULT_CONTENT_TYPE)) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    LoginDto loginDto = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), LoginDto.class);
    String username = loginDto.userId();
    String password = loginDto.password();
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
    return this.getAuthenticationManager().authenticate(authenticationToken);
  }
}
