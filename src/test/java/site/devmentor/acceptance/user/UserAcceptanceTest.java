package site.devmentor.acceptance.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.user.UserCreateRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 인수 테스트")
public class UserAcceptanceTest extends AcceptanceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void init() {
    userRepository.deleteAll();
    assertThat(userRepository.findAll()).isEmpty();
  }

  @Test
  void 회원가입_성공() throws Exception {
    // given
    UserCreateRequest userCreateRequest = new UserCreateRequest("user3", "user333333", "user3@dev.io");
    String userSignUpRequestAsString = new ObjectMapper().writeValueAsString(userCreateRequest);

    // when, then
    this.mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userSignUpRequestAsString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").doesNotExist())

    ;
  }

  @Test
  void 중복아이디_회원가입_실패() throws Exception {
    // given
    String givenUserId = "user1";
    saveOneUser(givenUserId, "user1@dev.io");
    UserCreateRequest userCreateRequest = new UserCreateRequest(givenUserId, "user333333", "user2@dev.io");
    String userSignUpRequestAsString = new ObjectMapper().writeValueAsString(userCreateRequest);

    // when, then
    this.mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userSignUpRequestAsString))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("아이디 '%s'는 이미 사용 중입니다.", givenUserId)))
    ;
  }

  @Test
  void 중복이메일_회원가입_실패() throws Exception{
    // given
    String givenEmail = "user1@dev.io";
    saveOneUser("user1", givenEmail);
    UserCreateRequest userCreateRequest = new UserCreateRequest("user2", "user333333", "user1@dev.io");
    String userSignUpRequestAsString = new ObjectMapper().writeValueAsString(userCreateRequest);

    // when, then
    this.mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userSignUpRequestAsString))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("이메일 '%s'는 이미 사용 중입니다.", givenEmail)));
  }

  @Test
  void 이메일중복확인_성공() throws Exception {
    // given
    String email = "user2@dev.io";

    // when, then
    this.mockMvc.perform(get("/user/email/{email}/exists", email))
            .andExpect(status().isOk());

  }

  @Test
  void 중복이메일_이메일중복확인_실패() throws Exception {
    // given, when
    String givenEmail = "user2@dev.io";
    userRepository.save(User.builder().userId("user1").email("user2@dev.io")
            .password("user2password123123!")
            .build());

    // then
    this.mockMvc.perform(get("/user/email/{email}/exists", givenEmail))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("이메일 '%s'는 이미 사용 중입니다.", givenEmail)));
  }

  @Test
  void 아이디중복확인_성공() throws Exception {
    // given, when
    String id = "user2";
    this.mockMvc.perform(get("/user/{id}/exists", id))
            .andExpect(status().isOk());
  }

  @Test
  void 중복아이디_아이디중복확인_실패() throws Exception {
    // given, when
    String givenUserId = "user1";
    userRepository.save(User.builder().userId(givenUserId).email("user2@dev.io")
            .password("user2password123123!")
            .build());

    // then
    this.mockMvc.perform(get("/user/{id}/exists", givenUserId))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").isBoolean())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").isString())
            .andExpect(jsonPath("$.message").value(String.format("아이디 '%s'는 이미 사용 중입니다.", givenUserId)));
  }

  private void saveOneUser(String userId, String email) {
    userRepository.save(
            User.builder()
                    .userId(userId)
                    .email(email)
                    .password("user1password1234")
                    .build());
  }
}