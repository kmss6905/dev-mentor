package site.devmentor.domain.mentor.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.dto.mentor.MentorRequestStatusDto;
import site.devmentor.exception.UnauthorizedAccessException;

import java.time.LocalDateTime;

@Table(name = "REQUEST")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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
            .status(Status.WAITING)
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

  private void verifyNotAccessOwnerMentor(AuthenticatedUser authUser) {
    if (this.toUserId != authUser.userPid()) {
      throw new UnauthorizedAccessException();
    }
  }

  private void verifyNotYetAccepted() {
    if (this.status.equals(Status.ACCEPTED)) {
      throw new IllegalStateException("이미 승인 된 멘토 요청에 대해서는 삭제가 불가능합니다.");
    }
  }

  public void changeStatus(AuthenticatedUser authUser, MentorRequestStatusDto requestStatus) {
    verifyNotAccessOwnerMentor(authUser);
    verifyNotYetDenied();
    verifyStatusChangeability(requestStatus.status());
    this.status = requestStatus.status();
  }

  private void verifyStatusChangeability(Status newStatus) {
    if (this.status.isNotChangeableTo(newStatus)) {
      throw new IllegalStateException(this.status.errorMessage());
    }
  }

  private void verifyNotYetDenied() {
    if (this.status == Status.DENIED) {
      throw new IllegalStateException("can change, because now status is = " + status.name());
    }
  }

  public long getFromUserId() {
    return fromUserId;
  }

  public Status getStatus() {
    return status;
  }
}
