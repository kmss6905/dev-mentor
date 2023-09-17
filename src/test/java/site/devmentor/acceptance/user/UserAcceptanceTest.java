package site.devmentor.acceptance.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.*;

@DisplayName("유저 인수 테스트")
public class UserAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void 회원가입_성공() throws Exception {
    // given
    String body = objectMapper.writeValueAsString(USER_ONE);

    // when, then
    mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").doesNotExist());
  }

  @Test
  void 중복아이디_회원가입_실패() throws Exception {
    // given
    userRepository.save(USER_ONE);
    String body = objectMapper.writeValueAsString(MAKE_DUPLICATED_ID_USER_REQUEST);

    // when, then
    this.mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("아이디 '%s'는 이미 사용 중입니다.", USER_ONE.getUserId())))
    ;
  }

  @Test
  void 중복이메일_회원가입_실패() throws Exception{
    // given
    userRepository.save(USER_ONE);
    String body = objectMapper.writeValueAsString(MAKE_DUPLICATED_EMAIL_USER_REQUEST);

    // when, then
    this.mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("이메일 '%s'는 이미 사용 중입니다.", MAKE_DUPLICATED_EMAIL_USER_REQUEST.getEmail())));
  }

  @Test
  void 이메일중복확인_성공() throws Exception {
    // given
    String email = NEW_USER_EMAIL;

    // when, then
    this.mockMvc.perform(get("/api/user/email/{email}/exists", email))
            .andExpect(status().isOk());

  }

  @Test
  void 중복이메일_이메일중복확인_실패() throws Exception {
    // given, when
    userRepository.save(USER_ONE);
    String givenEmail = USER_ONE.getEmail();

    // then
    this.mockMvc.perform(get("/api/user/email/{email}/exists", givenEmail))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("이메일 '%s'는 이미 사용 중입니다.", givenEmail)));
  }

  @Test
  void 아이디중복확인_성공() throws Exception {
    // given, when, then
    String userId = NEW_USER_ID;
    this.mockMvc.perform(get("/api/user/{id}/exists", userId))
            .andExpect(status().isOk());
  }

  @Test
  void 중복아이디_아이디중복확인_실패() throws Exception {
    // given, when
    userRepository.save(USER_ONE);

    // then
    this.mockMvc.perform(get("/api/user/{id}/exists", USER_ONE.getUserId()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("아이디 '%s'는 이미 사용 중입니다.", USER_ONE.getUserId())));
  }

  @Test
  void 회원가입_후_로그인_성공() throws Exception {
    // given
    String body = objectMapper.writeValueAsString(USER_ONE);

    // when, then
    mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").doesNotExist());


    // when, then
    String loginDtoReq = objectMapper.writeValueAsString(MAKE_USER_ONE_LOGIN_REQUEST);
    this.mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginDtoReq))
            .andExpect(status().isOk());
  }

  @Test
  void 잘못된_비밀번호로_로그인_실패() throws Exception{
    // given
    userRepository.save(USER_ONE);

    // when
    String loginDtoReq = objectMapper.writeValueAsString(MAKE_USER_ONE_WRONG_PASSWORD_REQUEST);

    // then
    this.mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginDtoReq))
            .andExpect(status().isUnauthorized());
  }
//
  @Test
  @WithMockUser("1")
  void 프로필_생성_성공() throws Exception {
    // given
    userRepository.save(USER_ONE);

    // when, then
    String body = objectMapper.writeValueAsString(MAKE_USER_PROFILE_REQUEST);
    this.mockMvc.perform(patch("/api/user/profile")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(USER_ONE.getId()))
            .andExpect(jsonPath("$.userId").value(USER_ONE.getUserId()))
            .andExpect(jsonPath("$.content").value(MAKE_USER_PROFILE_REQUEST.content()))
            .andExpect(jsonPath("$.createdAt", datetimeMatcher))
            .andExpect(jsonPath("$.updatedAt", datetimeMatcher));
  }

  @Test
  @WithMockUser(username = "1")
  void 프로필_삭제_성공() throws Exception {
    // given
    User user = userRepository.save(USER_ONE);
    user.updateProfile(MAKE_USER_PROFILE_REQUEST);

    // when, then
    this.mockMvc.perform(delete("/api/user/profile")
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
  }
}