package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.mentor.ScheduleDetailService;
import site.devmentor.application.mentor.ScheduleService;
import site.devmentor.auth.AppUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailStatus;
import site.devmentor.dto.Response;
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
      @Valid @RequestBody ScheduleRequest scheduleRequest
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
  public ResponseEntity<Void> removeSchedule(
      @LoginUser AppUser authUser,
      @PathVariable long id) {
    scheduleService.delete(authUser, id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/details")
  public ResponseEntity<ScheduleDetailCreateResponse> createDetail(
      @LoginUser AppUser authUser,
      @PathVariable long id,
      @Valid @RequestBody ScheduleDetailRequest scheduleDetailRequest
  ) {
    ScheduleDetailCreateResponse scheduleDetailCreateResponse = scheduleDetailService.createScheduleDetail(id, scheduleDetailRequest, authUser);
    return ResponseEntity.ok(scheduleDetailCreateResponse);
  }

  @PatchMapping("/details/{detailId}")
  public ResponseEntity<ScheduleDetailUpdateResponse> editDetail(
      @LoginUser AppUser appUser,
      @PathVariable long detailId,
      @Valid @RequestBody ScheduleDetailUpdateRequest detailUpdateRequest
  ) {
    return ResponseEntity.ok(scheduleDetailService.updateScheduleDetail(detailUpdateRequest, appUser, detailId));
  }

  @DeleteMapping("/details/{detailId}")
  public ResponseEntity<Void> removeDetail(
      @LoginUser AppUser appUser,
      @PathVariable long detailId
  ) {
    scheduleDetailService.deleteDetail(appUser, detailId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/details/{detailId}/status")
  public ResponseEntity<ScheduleDetailStatusUpdateResponse> changeDetailStatus(
      @LoginUser AppUser appUser,
      @PathVariable long detailId,
      @Valid @RequestBody ScheduleDetailStatusRequest detailStatusRequest
  ) {
    return ResponseEntity.ok(scheduleDetailService.updateDetailStatus(detailStatusRequest, appUser, detailId));
  }
}
