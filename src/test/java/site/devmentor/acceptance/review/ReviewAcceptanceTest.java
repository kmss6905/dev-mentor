package site.devmentor.acceptance.review;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.acceptance.AcceptanceTest;
import site.devmentor.acceptance.utils.Fixture;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.review.Rate;
import site.devmentor.domain.mentor.review.Review;
import site.devmentor.domain.mentor.review.ReviewRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devmentor.acceptance.utils.Fixture.MAKE_REVIEW_REQUEST;

@DisplayName("Review 인수테스트")
public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentorRequestRepository mentorRequestRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ReviewRepository reviewRepository;

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
    @WithMockUser(username = "2")
    @Transactional
    void 리뷰_생성_성공() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/mentor/review")
                .content(toBody(MAKE_REVIEW_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
