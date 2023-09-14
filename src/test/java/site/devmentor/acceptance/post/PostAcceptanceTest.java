package site.devmentor.acceptance.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.exception.UnauthorizedAccessException;
import site.devmentor.utill.PostFactory;
import site.devmentor.utill.UserFactory;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("포스트 인수 테스트")
public class PostAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  private final Matcher<String> datetimeMatcher = Matchers.matchesRegex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

  private final User user1 = UserFactory.user("user1");
  private final User user2 = UserFactory.user("user2");

  private Post savedPost;

  @BeforeAll
  void saveUser() {
    User user1Saved = userRepository.save(user1);
    userRepository.save(user2);
    savedPost = postRepository.save(PostFactory.post(user1Saved.getId()));
  }

  @Test
  void 포스트_생성_성공() throws Exception {
    // given
    PostCreateUpdateRequest postCreateUpdateRequest = new PostCreateUpdateRequest("title", "content");
    String body = new ObjectMapper().writeValueAsString(postCreateUpdateRequest);

    // when, then
    this.mockMvc.perform(post("/api/posts")
                    .with(user(String.valueOf(user1.getId())).password(user1.getPassword()))
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.postId").isNotEmpty());
  }

  @Test
  void 포스트_수정_성공() throws Exception{
    // given
    PostCreateUpdateRequest postCreateUpdateRequest = new PostCreateUpdateRequest("title1", "content1");
    String body = new ObjectMapper().writeValueAsString(postCreateUpdateRequest);

    // when, then
    this.mockMvc.perform(patch("/api/posts/{id}", savedPost.getId())
                    .with(user(String.valueOf(user1.getId())).password(user1.getPassword()))
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.createdAt", datetimeMatcher))
            .andExpect(jsonPath("$.updatedAt", datetimeMatcher))
            .andExpect(jsonPath("$.postId").value(savedPost.getId()))
            .andExpect(status().isOk());
  }

  @Test
  void 작성자_아닌_사람이_수정_실패() throws Exception {
    // given
    PostCreateUpdateRequest postCreateUpdateRequest = new PostCreateUpdateRequest("title1", "content1");
    String body = new ObjectMapper().writeValueAsString(postCreateUpdateRequest);

    // when, then
    this.mockMvc.perform(patch("/api/posts/{id}", savedPost.getId())
                    .with(user(String.valueOf(user2.getId())).password(user2.getPassword()))
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value(new UnauthorizedAccessException().getMessage()))
    ;
  }


  @Test
  void 비로그인_포스트_작성_실패() throws Exception {
    // given
    PostCreateUpdateRequest postCreateUpdateRequest = new PostCreateUpdateRequest("title", "content");
    String body = new ObjectMapper().writeValueAsString(postCreateUpdateRequest);

    // when, then
    this.mockMvc.perform(post("/api/post")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }
}
