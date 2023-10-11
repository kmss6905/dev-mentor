package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.ScheduleDetailRepository;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.dto.mentor.schedule.ScheduleDetailCreateResponse;
import site.devmentor.dto.mentor.schedule.ScheduleDetailRequest;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailUpdateResponse;
import site.devmentor.dto.mentor.schedule.ScheduleDetailUpdateRequest;
import site.devmentor.exception.UnauthorizedAccessException;
import site.devmentor.exception.schedule.ScheduleDetailNotFoundException;
import site.devmentor.exception.schedule.ScheduleNotFoundException;

@Service
public class ScheduleDetailService {

  private final ScheduleRepository scheduleRepository;
  private final ScheduleDetailRepository scheduleDetailRepository;

  public ScheduleDetailService(ScheduleRepository scheduleRepository, ScheduleDetailRepository scheduleDetailRepository) {
    this.scheduleRepository = scheduleRepository;
    this.scheduleDetailRepository = scheduleDetailRepository;
  }

  @Transactional
  public ScheduleDetailCreateResponse createScheduleDetail(long scheduleId, ScheduleDetailRequest detailRequest, AppUser appUser) {
    final ScheduleDetail scheduleDetail = convertToDetail(scheduleId, detailRequest, appUser);
    scheduleDetailRepository.save(scheduleDetail);
    return ScheduleDetailCreateResponse.of(scheduleId, scheduleDetail);
  }

  private ScheduleDetail convertToDetail(long scheduleId, ScheduleDetailRequest detailRequest, AppUser appUser) {
    final Schedule schedule = findSchedule(scheduleId);
    if (!schedule.isAuthorOf(appUser)) {
      throw new UnauthorizedAccessException();
    }
    return detailRequest.toDetailWithSchedule(schedule);
  }

  @Transactional
  public MentorScheduleDetailUpdateResponse updateScheduleDetail(ScheduleDetailUpdateRequest detailUpdateRequest, AppUser appUser, long detailId) {
    ScheduleDetail scheduleDetail = findDetailAuthorOf(detailId, appUser);
    scheduleDetail.update(detailUpdateRequest.toDetail());
    return MentorScheduleDetailUpdateResponse.of(scheduleDetail);
  }

  private ScheduleDetail findDetailAuthorOf(long detailId, AppUser appUser) {
    final ScheduleDetail detail = findDetail(detailId);
    if (!detail.isScheduleAuthor(appUser)) {
      throw new UnauthorizedAccessException();
    }
    return detail;
  }

  private ScheduleDetail findDetail(long detailId) {
    return scheduleDetailRepository.findById(detailId)
        .orElseThrow(() -> new ScheduleDetailNotFoundException(String.valueOf(detailId)));
  }

  private Schedule findSchedule(long scheduleId) {
    return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(String.valueOf(scheduleId)));
  }
}
