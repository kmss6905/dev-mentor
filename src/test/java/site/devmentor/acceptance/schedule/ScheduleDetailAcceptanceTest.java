package site.devmentor.acceptance.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.acceptance.utils.Fixture;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("스케줄 상세정보 인수테스트")
public class ScheduleDetailAcceptanceTest extends AcceptanceTest {

  @Autowired private UserRepository userRepository;
  @Autowired private MentorRequestRepository mentorRequestRepository;
  @Autowired private ScheduleRepository scheduleRepository;

  @BeforeEach
  void init() {
    User mentor = userRepository.save(Fixture.USER_ONE);
    User mentee = userRepository.save(Fixture.USER_TWO);
    MentorRequest mentorRequest = mentorRequestRepository.save(Fixture.MENTOR_REQUEST_ACCEPTED);

    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .mentor(mentor)
            .mentee(mentee)
            .build();

    scheduleRepository.saveAndFlush(schedule);
  }

  @Test
  @WithMockUser("1")
  @Transactional
  void 스케줄세부정보_추가_성공() throws Exception {
    MentorScheduleDetailDto makeScheduleDetailRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    Schedule schedule = scheduleRepository.findById(1L).get();
    assertThat(schedule.getDetails().size()).isEqualTo(1);
  }

  @Test
  void 비로그인_스케줄세부정보_추가_실패() throws Exception {
    MentorScheduleDetailDto makeScheduleDetailRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("2")
  void 권한없는_유저_스케줄세부정보_추가_실패() throws Exception {
    MentorScheduleDetailDto makeScheduleDetailRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

}
