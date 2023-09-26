package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.devmentor.application.mentor.ScheduleService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;

@RestController
@RequestMapping("/api/mentor/schedule")
public class MentorScheduleController {

  private final ScheduleService scheduleService;

  public MentorScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @PostMapping
  public ResponseEntity<?> createSchedule(
          @LoginUser AuthenticatedUser authUser,
          @Valid @RequestBody MentorScheduleDto mentorScheduleDto
  ) {
    return ResponseEntity.ok(scheduleService.create(authUser, mentorScheduleDto));
  }
}
