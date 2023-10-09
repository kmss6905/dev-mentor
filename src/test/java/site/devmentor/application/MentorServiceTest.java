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
  }

  @AfterEach
  public void after() {
    userRepository.deleteAll();
    mentorInfoRepository.deleteAll();
    mentorRequestRepository.deleteAll();
  }

  @Test
  void _100명_유저가_멘토요청_테스트() throws Exception {
    mentorInfoRepository.saveAndFlush(MENTOR_INFO);

    int threadCount = 100; // 100명의 요청
    MentorRequestDto mentorRequestDto = new MentorRequestDto(1, "멘토요청 받아주세요");

    ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드 풀의 사이즈는 10
    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) { // 100번의 요청 시행
      executorService.submit(() -> {
        try {
          mentorRequestService.request(new AuthenticatedUser(1L), mentorRequestDto); // 멘토 요청
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          latch.countDown(); // 요청이 끝날 때마다 countDown();
        }
      });
    }
    latch.await(); // 스레드에 할당 된 작업들이 다 끝날 때 까지 기다립니다.
    MentorInfo mentorInfo = mentorInfoRepository.findByUserId(1L);

    // 100건의 요청 따라서 100건의 요청, 현재 맨티 수는 100명
    assertEquals(100, mentorRequestRepository.findAll().size());
    assertEquals(100, mentorInfo.getCurrentMentees());
  }

  @Test
  @DisplayName("10,000 명의 유저가 멘토요청을 합니다.")
  void concurrencyTest() throws InterruptedException {
    mentorInfoRepository.saveAndFlush(
            MentorInfo.builder()
                    .currentMentees(0L)
                    .maxMentees(10_000L)
                    .userId(1L)
                    .build());

    int threadCount = 10_000; // 1만명 요청
    MentorRequestDto mentorRequestDto = new MentorRequestDto(1, "멘토요청 받아주세요");

    ExecutorService executorService = Executors.newFixedThreadPool(32); // 스레드 풀의 사이즈는 32
    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) { // 10_000번의 요청 시행
      executorService.submit(() -> {
        try {
          mentorRequestService.request(new AuthenticatedUser(1L), mentorRequestDto); // 멘토 요청
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          latch.countDown(); // 요청이 끝날 때마다 countDown();
        }
      });
    }
    latch.await(); // 스레드에 할당 된 작업들이 다 끝날 때 까지 기다립니다.
    MentorInfo mentorInfo = mentorInfoRepository.findByUserId(1L);

    // 100건의 요청 따라서 100건의 요청, 현재 맨티 수는 100명
    assertEquals(10_000, mentorRequestRepository.findAll().size());
    assertEquals(10_000, mentorInfo.getCurrentMentees());
  }
}
