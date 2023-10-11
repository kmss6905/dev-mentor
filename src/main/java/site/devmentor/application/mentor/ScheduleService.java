package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.ScheduleRequest;
import site.devmentor.dto.mentor.schedule.ScheduleResponse;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;

@Service
public class ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final MentorRequestRepository mentorRequestRepository;
  private final UserRepository userRepository;

  public ScheduleService(ScheduleRepository scheduleRepository, MentorRequestRepository mentorRequestRepository, UserRepository userRepository) {
    this.scheduleRepository = scheduleRepository;
    this.mentorRequestRepository = mentorRequestRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public ScheduleResponse createSchedule(AppUser appUser, ScheduleRequest scheduleRequest) {
    MentorRequest mentorRequest = findRequest(scheduleRequest.requestId());
    User mentee = findUser(mentorRequest.getMenteeUserId());
    User mentor = findUser(appUser.pid());
    Schedule schedule = scheduleRequest.toSchedule(mentor, mentee, mentorRequest);
    Schedule savedSchedule = scheduleRepository.save(schedule);
    return ScheduleResponse.of(savedSchedule);
  }

  private MentorRequest findRequest(long requestId) {
    return mentorRequestRepository.findById(requestId).orElseThrow(IllegalStateException::new);
  }

  private User findUser(long id) {
    return userRepository.findById(id).orElseThrow(IllegalStateException::new);
  }

  @Transactional
  public void update(AppUser authUser, MentorScheduleUpdateDto mentorScheduleUpdateDto, long id) {
    Schedule schedule = findSchedule(id);
    schedule.update(authUser, mentorScheduleUpdateDto);
  }

  private Schedule findSchedule(long id) {
    return scheduleRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
  }

  @Transactional
  public void delete(AppUser authUser, long id) {
    Schedule schedule = findSchedule(id);
    schedule.delete(authUser);
  }
}
