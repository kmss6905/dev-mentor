package site.devmentor.dto.mentor;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import site.devmentor.domain.mentor.request.MentorRequest;

@Builder
public record MentorRequestDto(
        long mentorUserId,
        @NotNull(message = "[memo]입력은 필수입니다.") String memo
){
  public MentorRequest to() {
    return null;
  }
}