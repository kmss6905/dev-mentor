package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.schedule.SchduleResponse;

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

  public SchduleResponse create(AuthenticatedUser authUser, MentorScheduleDto mentorScheduleDto) {
    MentorRequest mentorRequest = mentorRequestRepository.findById(mentorScheduleDto.getRequestId()).orElseThrow(IllegalStateException::new);
    User mentee = userRepository.findById(mentorRequest.getFromUserId()).orElseThrow(IllegalStateException::new);
    User mentor = userRepository.findById(authUser.userPid()).orElseThrow(IllegalStateException::new);
    Schedule schedule = Schedule.create(mentor, mentee, mentorRequest, mentorScheduleDto);
    Schedule save = scheduleRepository.save(schedule);
    return new SchduleResponse(save.getId());
  }
}
