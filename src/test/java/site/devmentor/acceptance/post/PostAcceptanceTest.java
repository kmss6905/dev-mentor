package site.devmentor.acceptance.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.post.PostCreateRequest;
import site.devmentor.utill.UserFactory;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("포스트 인수 테스트")
public class PostAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void 포스트_생성_성공() throws Exception {
    // given
    User user = UserFactory.user();
    User savedUser = userRepository.save(UserFactory.user());
    PostCreateRequest postCreateRequest = new PostCreateRequest("title", "content");
    String body = new ObjectMapper().writeValueAsString(postCreateRequest);

    // when, then
    this.mockMvc.perform(post("/api/post")
                    .with(user(String.valueOf(savedUser.getId())).password(user.getPassword()))
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated());
  }

  @Test
  void 비로그인_포스트_생성_실패() throws Exception {
    // given
    PostCreateRequest postCreateRequest = new PostCreateRequest("title", "content");
    String body = new ObjectMapper().writeValueAsString(postCreateRequest);

    // when, then
    this.mockMvc.perform(post("/api/post")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }
}
