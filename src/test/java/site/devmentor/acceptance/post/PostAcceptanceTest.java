package site.devmentor.acceptance.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.acceptance.utils.Fixture;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.exception.UnauthorizedAccessException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.USER_TWO;

@DisplayName("포스트 인수 테스트")
public class PostAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  private final Matcher<String> datetimeMatcher = Matchers.matchesRegex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

  @BeforeEach
  void setup() {
    userRepository.save(Fixture.USER_ONE);
    userRepository.save(USER_TWO);
    postRepository.save(Fixture.USER_ONE_POST);
  }

  @Test
  @WithMockUser(username = "1")
  void 포스트_생성_성공() throws Exception {
    // given
    String body = new ObjectMapper().writeValueAsString(Fixture.MAKE_POST_REQUEST);

    // when, then
    this.mockMvc.perform(post("/api/posts")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.postId").isNotEmpty());
  }

  @Test
  @WithMockUser(username = "1")
  void 포스트_수정_성공() throws Exception{
    // given
    String body = new ObjectMapper().writeValueAsString(Fixture.MAKE_POST_REQUEST);

    // when, then
    this.mockMvc.perform(patch("/api/posts/{id}", Fixture.USER_ONE_POST.getId())
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.createdAt", datetimeMatcher))
            .andExpect(jsonPath("$.updatedAt", datetimeMatcher))
            .andExpect(jsonPath("$.postId").value(Fixture.USER_ONE_POST.getId()))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "2")
  void 작성자_아닌_사람이_수정_실패() throws Exception {
    // given
    String body = objectMapper.writeValueAsString(Fixture.MAKE_POST_REQUEST);

    // when, then
    this.mockMvc.perform(patch("/api/posts/{id}", Fixture.USER_ONE_POST.getId())
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value(new UnauthorizedAccessException().getMessage()));
  }


  @Test
  void 비로그인_포스트_작성_실패() throws Exception {
    // given
    String body = objectMapper.writeValueAsString(Fixture.MAKE_POST_REQUEST);

    // when, then
    this.mockMvc.perform(post("/api/post")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }


  @Test
  @WithMockUser(username = "1")
  void 포스트_삭제_성공() throws Exception{
    this.mockMvc.perform(delete("/api/posts/{id}", Fixture.USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "2")
  void 작성자_아닌_사람_포스트_삭제_실패() throws Exception{
    this.mockMvc.perform(delete("/api/posts/{id}", Fixture.USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void 비로그인_삭제_실패() throws Exception {
    this.mockMvc.perform(delete("/api/posts/{id}", Fixture.USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }
}