package site.devmentor.acceptance.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.acceptance.utils.Fixture;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.domain.user.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.*;


@DisplayName("포스트 댓글 인수테스트")
public class CommentAcceptanceTest extends AcceptanceTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void each() {
    userRepository.save(USER_ONE);
    postRepository.save(USER_ONE_POST);
  }

  @Test
  @WithMockUser("1")
  void 포스트_댓글_작성_성공() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(post("/api/posts/{id}/comments", USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.commentId").value(1L))
            .andExpect(jsonPath("$.comment").value(MAKE_COMMENT_REQUEST.comment()));
  }

  @Test
  void 비로그인_포스트_댓글_작성_실패() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(post("/api/posts/{id}/comments", USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isForbidden());
  }
}
