package site.devmentor.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.mentor.MentorRequestService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.dto.mentor.MentorRequestStatusDto;

@RestController
@RequestMapping("/api/mentor/requests")
public class MentorRequestController {

  private final MentorRequestService mentorRequestService;

  public MentorRequestController(MentorRequestService mentorRequestService) {
    this.mentorRequestService = mentorRequestService;
  }

  @PostMapping
  public ResponseEntity<?> createMentorRequest(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody MentorRequestDto mentorRequestDto
  ) {
    mentorRequestService.request(authUser, mentorRequestDto);
    return ResponseUtil.ok();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMentorRequest(
          @LoginUser AuthenticatedUser authUser,
          @PathVariable long id
  ) {
    mentorRequestService.delete(authUser, id);
    return ResponseUtil.ok();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody MentorRequestStatusDto requestStatus,
          @PathVariable long id
  ) {
    mentorRequestService.update(authUser, requestStatus, id);
    return ResponseUtil.ok();
  }
}
