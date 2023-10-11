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
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.ScheduleDetailRepository;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailTime;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMenteeMemo;
import site.devmentor.domain.mentor.schedule.vo.ScheduleMentorMemo;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.ScheduleDetailRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST;

@DisplayName("스케줄 상세정보 인수테스트")
public class ScheduleDetailRequestAcceptanceTest extends AcceptanceTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MentorRequestRepository mentorRequestRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;
  @Autowired
  private ScheduleDetailRepository scheduleDetailRepository;

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
        .author(mentor)
        .mentee(mentee)
        .build();

    scheduleRepository.saveAndFlush(schedule);
  }

  @Test
  @WithMockUser("1")
  @Transactional
  void 스케줄세부정보_추가_성공() throws Exception {
    ScheduleDetailRequest makeScheduleDetailRequestRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequestRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    ScheduleDetail scheduleDetail = scheduleDetailRepository.findById(1L).get();
    assertThat(scheduleDetail.getId()).isEqualTo(1L);
  }

  @Test
  void 비로그인_스케줄세부정보_추가_실패() throws Exception {
    ScheduleDetailRequest makeScheduleDetailRequestRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequestRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("2")
  void 권한없는_유저_스케줄세부정보_추가_실패() throws Exception {
    ScheduleDetailRequest makeScheduleDetailRequestRequest = Fixture.MAKE_SCHEDULE_DETAIL_REQUEST;
    String body = objectMapper.writeValueAsString(makeScheduleDetailRequestRequest);
    mockMvc.perform(post("/api/mentor/schedules/{id}/details", 1L)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser("1")
  @Transactional
  void 스케줄세부정보_수정_성공() throws Exception {
    // given
    saveScheduleDetail();

    ScheduleDetailRequest updateRequest = MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST;
    String body = objectMapper.writeValueAsString(updateRequest);

    // when
    mockMvc.perform(patch("/api/mentor/schedules/{id}/details/{detailId}", 1L, 1L)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.detailId").value(1L));

    // then
    ScheduleDetail scheduleDetail1 = scheduleDetailRepository.findById(1L).get();
    assertThat(scheduleDetail1.getId()).isEqualTo(1L);
    assertThat(scheduleDetail1.getTime().getEndTime()).isEqualTo(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST.endDate());
    assertThat(scheduleDetail1.getTime().getStartTime()).isEqualTo(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST.startDate());
    assertThat(scheduleDetail1.getTitle()).isEqualTo(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST.title());
    assertThat(scheduleDetail1.getMemo().getToMenteeMemo()).isEqualTo(ScheduleMenteeMemo.from(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST.menteeMemo()));
    assertThat(scheduleDetail1.getMemo().getToMentorMemo()).isEqualTo(ScheduleMentorMemo.from(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST.mentorMemo()));
  }

  @Test
  @WithMockUser("2")
  void 권한없는_유저_스케줄세부정보_수정_실패() throws Exception {
    // given
    saveScheduleDetail();

    String body = toBody(MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST);
    mockMvc.perform(patch("/api/mentor/schedules/{id}/details/{detailId}", 1L, 1L)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  private void saveScheduleDetail() {
    Schedule schedule = scheduleRepository.findById(1L).get();
    ScheduleDetail scheduleDetail = ScheduleDetail.builder()
        .title("title")
        .memo(ScheduleDetailMemo.create(
            ScheduleMentorMemo.from("mentorMemo"),
            ScheduleMenteeMemo.from("menteeMemo")
        )).time(ScheduleDetailTime
            .of(
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 10, 0, 0))
        ).schedule(schedule)
        .build();
    scheduleDetailRepository.saveAndFlush(scheduleDetail);
  }
}