package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailCreateResponse;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailDto;
import site.devmentor.exception.schedule.ScheduleNotFoundException;

@Service
public class ScheduleDetailService {

  private final ScheduleRepository scheduleRepository;

  public ScheduleDetailService(ScheduleRepository scheduleRepository) {
    this.scheduleRepository = scheduleRepository;
  }

  @Transactional
  public MentorScheduleDetailCreateResponse create(MentorScheduleDetailDto mentorScheduleDetailDto, AuthenticatedUser authUser, long id) {
    Schedule schedule = findSchedule(id);
    ScheduleDetail scheduleDetail = ScheduleDetail.create(mentorScheduleDetailDto);
    schedule.addDetail(scheduleDetail, authUser);
    return new MentorScheduleDetailCreateResponse(schedule.getId(), scheduleDetail.getId(), scheduleDetail.getCreatedAt());
  }

  private Schedule findSchedule(long id) {
    return scheduleRepository.findById(id)
            .orElseThrow(() -> new ScheduleNotFoundException(String.valueOf(id)));
  }
}
