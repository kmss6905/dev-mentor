package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.mentor.ScheduleDetailService;
import site.devmentor.application.mentor.ScheduleService;
import site.devmentor.auth.AppUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.mentor.schedule.*;

@RestController
@RequestMapping("/api/mentor/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;
  private final ScheduleDetailService scheduleDetailService;

  public ScheduleController(ScheduleService scheduleService, ScheduleDetailService scheduleDetailService) {
    this.scheduleService = scheduleService;
    this.scheduleDetailService = scheduleDetailService;
  }

  @PostMapping
  public ResponseEntity<ScheduleResponse> createSchedule(
          @LoginUser AppUser appUser,
          @RequestBody ScheduleRequest scheduleRequest
  ) {
    return ResponseEntity.ok(scheduleService.createSchedule(appUser, scheduleRequest));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> editSchedule(
          @LoginUser AppUser authUser,
          @Valid @RequestBody MentorScheduleUpdateDto mentorScheduleUpdateDto,
          @PathVariable long id) {
    scheduleService.update(authUser, mentorScheduleUpdateDto, id);
    return ResponseUtil.ok();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeSchedule(
          @LoginUser AppUser authUser,
          @PathVariable long id) {
    scheduleService.delete(authUser, id);
    return ResponseUtil.ok();
  }

  @PostMapping("/{id}/details")
  public ResponseEntity<ScheduleDetailCreateResponse> createDetail(
          @LoginUser AppUser authUser,
          @PathVariable long id,
          @RequestBody ScheduleDetailRequest scheduleDetailRequest
  ) {
    ScheduleDetailCreateResponse scheduleDetailCreateResponse = scheduleDetailService.createScheduleDetail(id, scheduleDetailRequest, authUser);
    return ResponseEntity.ok(scheduleDetailCreateResponse);
  }

  @PatchMapping("/details/{detailId}")
  public ResponseEntity<MentorScheduleDetailUpdateResponse> editDetail(
          @LoginUser AppUser appUser,
          @PathVariable long detailId,
          @Valid @RequestBody ScheduleDetailUpdateRequest detailUpdateRequest
  ) {
    return ResponseEntity.ok(scheduleDetailService.updateScheduleDetail(detailUpdateRequest, appUser, detailId));
  }
}
