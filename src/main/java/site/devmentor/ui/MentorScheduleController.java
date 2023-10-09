package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.mentor.ScheduleDetailService;
import site.devmentor.application.mentor.ScheduleService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleResponse;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;

@RestController
@RequestMapping("/api/mentor/schedules")
public class MentorScheduleController {

  private final ScheduleService scheduleService;
  private final ScheduleDetailService scheduleDetailService;

  public MentorScheduleController(ScheduleService scheduleService, ScheduleDetailService scheduleDetailService) {
    this.scheduleService = scheduleService;
    this.scheduleDetailService = scheduleDetailService;
  }

  @PostMapping
  public ResponseEntity<MentorScheduleResponse> createSchedule(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody MentorScheduleDto mentorScheduleDto
  ) {
    return ResponseEntity.ok(scheduleService.create(authUser, mentorScheduleDto));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> editSchedule(
          @LoginUser AuthenticatedUser authUser,
          @Valid @RequestBody MentorScheduleUpdateDto mentorScheduleUpdateDto,
          @PathVariable long id) {
    scheduleService.update(authUser, mentorScheduleUpdateDto, id);
    return ResponseUtil.ok();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeSchedule(
          @LoginUser AuthenticatedUser authUser,
          @PathVariable long id) {
    scheduleService.delete(authUser, id);
    return ResponseUtil.ok();
  }

  @PostMapping("/{id}/details")
  public ResponseEntity<?> createDetail(
          @LoginUser AuthenticatedUser authUser,
          @PathVariable long id,
          @RequestBody MentorScheduleDetailDto mentorScheduleDetailDto
  ) {
    return ResponseEntity.ok(scheduleDetailService.create(mentorScheduleDetailDto, authUser, id));
  }
}
