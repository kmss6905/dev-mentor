package site.devmentor.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.devmentor.application.mentor.MentorRequestService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.mentor.MentorRequestDto;

@RestController
@RequestMapping("/api/mentor/requests")
public class MentorRequestController {

  private final MentorRequestService mentorRequestService;

  public MentorRequestController(MentorRequestService mentorRequestService) {
    this.mentorRequestService = mentorRequestService;
  }

  @PostMapping
  public ResponseEntity<?> mentorReq(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody MentorRequestDto mentorRequestDto
  ) {
    mentorRequestService.request(authUser, mentorRequestDto);
    return ResponseEntity.ok().body("ok");
  }
}
