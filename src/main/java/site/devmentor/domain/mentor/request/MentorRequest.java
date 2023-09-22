package site.devmentor.domain.mentor.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.exception.UnauthorizedAccessException;

import java.time.LocalDateTime;

@Table(name = "REQUEST")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentorRequest extends BaseEntity {

  @Column(name = "from_user_id")
  private long fromUserId;
  @Column(name = "to_user_id")
  private long toUserId;

  @Enumerated(value = EnumType.STRING)
  private Status status = Status.WAITING;

  @Enumerated
  private Memo memo;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  protected MentorRequest(final long fromUserId, final long toUserId, final Memo memo, final Status status) {
    this.memo = memo;
    this.toUserId = toUserId;
    this.fromUserId = fromUserId;
    this.status = status;
  }

  public static MentorRequest create(AuthenticatedUser authUser, MentorRequestDto mentorRequestDto) {
    return MentorRequest.builder()
            .fromUserId(authUser.userPid())
            .toUserId(mentorRequestDto.mentorUserId())
            .memo(new Memo(mentorRequestDto.memo()))
            .build();
  }

  public void verifyCanDelete(AuthenticatedUser authUser) {
    verifyNotAccessOwner(authUser);
    verifyNotYetAccepted();
  }

  private void verifyNotAccessOwner(AuthenticatedUser authUser) {
    if (this.fromUserId != authUser.userPid()) {
      throw new UnauthorizedAccessException();
    }
  }

  private void verifyNotYetAccepted() {
    if (status.equals(Status.ACCEPTED)) {
      throw new IllegalStateException("이미 승인 된 멘토 요청에 대해서는 삭제가 불가능합니다.");
    }
  }
}
