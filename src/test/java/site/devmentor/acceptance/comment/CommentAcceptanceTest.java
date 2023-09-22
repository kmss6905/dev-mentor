package site.devmentor.acceptance.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.comment.CommentRepository;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.exception.UnauthorizedAccessException;
import site.devmentor.exception.comment.CommentNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.*;


@DisplayName("포스트 댓글 인수테스트")
public class CommentAcceptanceTest extends AcceptanceTest {

  public static final String UNAUTHORIZED_ACCESS_ERROR_MESSAGE = new UnauthorizedAccessException().getMessage();
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;

  @BeforeEach
  void each() {
    userRepository.save(USER_ONE);
    postRepository.save(USER_ONE_POST);
    commentRepository.save(USER_ONE_POST_COMMENT);
  }

  @Test
  @WithMockUser("1")
  void 포스트_댓글_작성_성공() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(post("/api/posts/{id}/comments", USER_ONE_POST.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.commentId").value(2L))
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

  @Test
  @WithMockUser(username = "1")
  void 포스트_댓글_수정() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(patch("/api/posts/{id}/comments/{id}", 1L, 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comment").value(MAKE_COMMENT_REQUEST.comment()))
            .andExpect(jsonPath("$.commentId").value(1L))
            .andExpect(jsonPath("$.updatedAt", datetimeMatcher));
  }

  @Test
  @WithMockUser(username = "2")
  void 작성자_아닌_사람_댓글_수정_실패() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(patch("/api/posts/{id}/comments/{id}", 1L, 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value(UNAUTHORIZED_ACCESS_ERROR_MESSAGE));
  }

  @Test
  @WithMockUser(username = "1")
  void 없는_댓글_수정_실패() throws Exception {
    String body = objectMapper.writeValueAsString(MAKE_COMMENT_REQUEST);
    mockMvc.perform(patch("/api/posts/{id}/comments/{id}", 1L, 100L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value(new CommentNotFoundException("100").getMessage()));
  }
}
