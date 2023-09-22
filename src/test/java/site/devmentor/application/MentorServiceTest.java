package site.devmentor.application;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.devmentor.application.mentor.MentorRequestService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.info.MentorInfo;
import site.devmentor.domain.mentor.info.MentorInfoRepository;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.MentorRequestDto;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static site.devmentor.acceptance.utils.Fixture.*;

@SpringBootTest
public class MentorServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MentorInfoRepository mentorInfoRepository;

  @Autowired
  private MentorRequestRepository mentorRequestRepository;

  @Autowired
  private MentorRequestService mentorRequestService;

  @BeforeEach
  public void setUp() {
    userRepository.save(USER_ONE);
    userRepository.save(USER_TWO);
    mentorInfoRepository.save(MENTOR_INFO);
  }

  @AfterEach
  public void after() {
    userRepository.deleteAll();
    mentorInfoRepository.deleteAll();
    mentorRequestRepository.deleteAll();
  }

  @Test
  void testApiConcurrentRequests() throws Exception {
    int threadCount = 100;

    MentorRequestDto mentorRequestDto = new MentorRequestDto(1, "멘토요청 받아주세요");

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          mentorRequestService.request(new AuthenticatedUser(1L), mentorRequestDto);
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    MentorInfo mentorInfo = mentorInfoRepository.findByUserId(1L);
    assertEquals(100, mentorInfo.getCurrentMentees());
    assertEquals(100, mentorRequestRepository.findAll().size());
  }
}
