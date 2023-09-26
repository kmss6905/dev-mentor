package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.mentor.ScheduleService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.Response;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleResponse;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;

@RestController
@RequestMapping("/api/mentor/schedules")
public class MentorScheduleController {

  private final ScheduleService scheduleService;

  public MentorScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @PostMapping
  public ResponseEntity<MentorScheduleResponse> createSchedule(
          @LoginUser AuthenticatedUser authUser,
          @Valid @RequestBody MentorScheduleDto mentorScheduleDto
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
}
