package site.devmentor.acceptance.mentor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.domain.mentor.info.MentorInfoRepository;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.*;

@DisplayName("멘토요청 인수테스트")
public class MentorRequestAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MentorRequestRepository mentorRequestRepository;

  @Autowired
  private MentorInfoRepository mentorInfoRepository;

  @BeforeEach
  void before() {
    userRepository.save(USER_ONE);
    userRepository.save(USER_TWO);
  }

  @Test
  @WithMockUser(username = "2")
  void 멘토요청_성공() throws Exception {
    // given
    mentorInfoRepository.save(MENTOR_INFO);

    // when, then
    String body = objectMapper.writeValueAsString(MENTOR_CREATE_REQUEST);
    this.mockMvc.perform(post("/api/mentor/requests")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "2")
  void 허용치이상_멘토요청_실패() throws Exception {
    // given
    mentorInfoRepository.save(FULL_MENTOR_INFO);

    // when, then
    String body = objectMapper.writeValueAsString(MENTOR_CREATE_REQUEST);
    this.mockMvc.perform(post("/api/mentor/requests")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }

  @Test
  void 비로그인_멘토요청_실패() throws Exception {
    // given
    mentorInfoRepository.save(MENTOR_INFO);

    // when, then
    String body = objectMapper.writeValueAsString(MENTOR_CREATE_REQUEST);
    this.mockMvc.perform(post("/api/mentor/requests")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "2")
  void 멘토요청_삭제_성공() throws Exception {
    // given
    mentorInfoRepository.save(MENTOR_INFO);
    MentorRequest savedMentorRequest = mentorRequestRepository.save(MENTOR_REQUEST);

    // when, then
    this.mockMvc.perform(delete("/api/mentor/requests/{id}", savedMentorRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  @WithMockUser(username = "2")
  void 승인된_멘토요청_삭제_실패() throws Exception {
    // given
    mentorInfoRepository.save(MENTOR_INFO);
    MentorRequest savedMentorRequest = mentorRequestRepository.save(MENTOR_REQUEST_ACCEPTED);

    // when, then
    this.mockMvc.perform(delete("/api/mentor/requests/{id}", savedMentorRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("이미 승인 된 멘토 요청에 대해서는 삭제가 불가능합니다."));
  }

  @Test
  @WithMockUser(username = "1")
  void 다른유저_멘토요청_삭제_실패() throws Exception {
    // given
    mentorInfoRepository.save(MENTOR_INFO);
    MentorRequest savedMentorRequest = mentorRequestRepository.save(MENTOR_REQUEST_ACCEPTED);

    // when, then
    this.mockMvc.perform(delete("/api/mentor/requests/{id}", savedMentorRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false));
  }
}
