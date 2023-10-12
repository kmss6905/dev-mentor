package site.devmentor.acceptance.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.acceptance.utils.Fixture;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.ScheduleRequest;
import site.devmentor.dto.mentor.schedule.ScheduleUpdateDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Schedule 인수 테스트")
class ScheduleAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MentorRequestRepository mentorRequestRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @BeforeEach
  void init() {
    userRepository.save(Fixture.USER_ONE);
    userRepository.save(Fixture.USER_TWO);
    mentorRequestRepository.save(Fixture.MENTOR_REQUEST_ACCEPTED);
  }

  @Test
  @WithMockUser("1")
  void 스케줄_생성_성공() throws Exception {
    ScheduleRequest scheduleRequest = Fixture.MAKE_SCHEDULE_REQUEST;
    String body = objectMapper.writeValueAsString(scheduleRequest);

    mockMvc.perform(post("/api/mentor/schedules")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk());
  }

  @Test
  void 비로그인_스케쥴_생성_실패() throws Exception {
    ScheduleRequest scheduleRequest = Fixture.MAKE_SCHEDULE_REQUEST;
    String body = objectMapper.writeValueAsString(scheduleRequest);

    mockMvc.perform(post("/api/mentor/schedules")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("1")
  void 스케줄_수정_성공() throws Exception {
    // given
    User mentor = userRepository.findById(1L).get();
    User mentee = userRepository.findById(2L).get();
    MentorRequest mentorRequest = mentorRequestRepository.findById(1L).get();

    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .author(mentor)
            .mentee(mentee)
            .build();
    scheduleRepository.saveAndFlush(schedule);

    // when
    ScheduleUpdateDto updateDto = Fixture.MAKE_SCHEDULE_UPDATE_REQUEST;
    String body = objectMapper.writeValueAsString(updateDto);


    // then
    mockMvc.perform(patch("/api/mentor/schedules/{id}", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    Schedule schedule1 = scheduleRepository.findById(1L).get();
    assertThat(schedule1.getContent().getTitle()).isEqualTo(Fixture.MAKE_SCHEDULE_UPDATE_REQUEST.title());
    assertThat(schedule1.getContent().getMemo()).isEqualTo(Fixture.MAKE_SCHEDULE_UPDATE_REQUEST.memo());
    assertThat(schedule1.getTime().getStart()).isEqualTo(Fixture.MAKE_SCHEDULE_UPDATE_REQUEST.startDate());
    assertThat(schedule1.getTime().getEnd()).isEqualTo(Fixture.MAKE_SCHEDULE_UPDATE_REQUEST.endDate());
  }

  @Test
  void 비로그인_스케줄_수정_실패() throws Exception {
    // given
    User mentor = userRepository.findById(1L).get();
    User mentee = userRepository.findById(2L).get();
    MentorRequest mentorRequest = mentorRequestRepository.findById(1L).get();

    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .author(mentor)
            .mentee(mentee)
            .build();
    scheduleRepository.saveAndFlush(schedule);

    // when
    ScheduleUpdateDto updateDto = Fixture.MAKE_SCHEDULE_UPDATE_REQUEST;
    String body = objectMapper.writeValueAsString(updateDto);


    // then
    mockMvc.perform(patch("/api/mentor/schedules/{id}", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
  }


  @Test
  @WithMockUser("2")
  void 권한없는_유저_스케줄_수정_실패() throws Exception {
    // given
    User mentor = userRepository.findById(1L).get();
    User mentee = userRepository.findById(2L).get();
    MentorRequest mentorRequest = mentorRequestRepository.findById(1L).get();

    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .author(mentor)
            .mentee(mentee)
            .build();
    scheduleRepository.saveAndFlush(schedule);

    // when
    ScheduleUpdateDto updateDto = Fixture.MAKE_SCHEDULE_UPDATE_REQUEST;
    String body = objectMapper.writeValueAsString(updateDto);


    // then
    mockMvc.perform(patch("/api/mentor/schedules/{id}", 1L)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser("1")
  void 스케줄_삭제_성공() throws Exception {
    // given
    User mentor = userRepository.findById(1L).get();
    User mentee = userRepository.findById(2L).get();
    MentorRequest mentorRequest = mentorRequestRepository.findById(1L).get();
    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .author(mentor)
            .mentee(mentee)
            .build();
    scheduleRepository.saveAndFlush(schedule);

    // then
    mockMvc.perform(delete("/api/mentor/schedules/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    Optional<Schedule> schedule1 = scheduleRepository.findById(1L);
    assertThat(schedule1.isEmpty()).isTrue();
  }

  @Test
  @WithMockUser("2")
  void 권한_없는_유저_스케줄_삭제_실패() throws Exception {
    // given
    User mentor = userRepository.findById(1L).get();
    User mentee = userRepository.findById(2L).get();
    MentorRequest mentorRequest = mentorRequestRepository.findById(1L).get();
    Schedule schedule = Schedule.builder()
            .title("title")
            .memo("memo")
            .startTime(LocalDateTime.of(2023, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2023, 1, 10, 0, 0))
            .request(mentorRequest)
            .author(mentor)
            .mentee(mentee)
            .build();
    scheduleRepository.saveAndFlush(schedule);

    // when, then
    mockMvc.perform(delete("/api/mentor/schedules/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

}
