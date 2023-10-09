package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleResponse;
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

  public MentorScheduleResponse create(AuthenticatedUser authUser, MentorScheduleDto mentorScheduleDto) {
    MentorRequest mentorRequest = findMentorRequest(mentorScheduleDto.requestId());
    User mentee = findUser(mentorRequest.getFromUserId());
    User mentor = findUser(authUser.userPid());
    Schedule schedule = Schedule.create(mentor, mentee, mentorRequest, mentorScheduleDto);
    Schedule save = scheduleRepository.save(schedule);
    return new MentorScheduleResponse(save.getId());
  }

  private MentorRequest findMentorRequest(long requestId) {
    return mentorRequestRepository.findById(requestId).orElseThrow(IllegalStateException::new);
  }

  private User findUser(long id) {
    return userRepository.findById(id).orElseThrow(IllegalStateException::new);
  }

  @Transactional
  public void update(AuthenticatedUser authUser, MentorScheduleUpdateDto mentorScheduleUpdateDto, long id) {
    Schedule schedule = findSchedule(id);
    schedule.update(authUser, mentorScheduleUpdateDto);
  }

  private Schedule findSchedule(long id) {
    return scheduleRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
  }

  @Transactional
  public void delete(AuthenticatedUser authUser, long id) {
    Schedule schedule = findSchedule(id);
    schedule.delete(authUser);
  }
}
