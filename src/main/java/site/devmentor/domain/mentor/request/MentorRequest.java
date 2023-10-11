package site.devmentor.domain.mentor.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import site.devmentor.auth.AppUser;
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
  private long menteeUserId;
  @Column(name = "to_user_id")
  private long mentorUserId;

  @Enumerated(value = EnumType.STRING)
  private Status status = Status.WAITING;

  @Embedded
  private Memo memo;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  protected MentorRequest(final long menteeUserId, final long mentorUserId, final Memo memo, final Status status) {
    this.memo = memo;
    this.mentorUserId = mentorUserId;
    this.menteeUserId = menteeUserId;
    this.status = status;
  }

  public static MentorRequest create(AppUser authUser, MentorRequestDto mentorRequestDto) {
    return MentorRequest.builder()
            .mentorUserId(authUser.pid())
            .menteeUserId(mentorRequestDto.mentorUserId())
            .memo(new Memo(mentorRequestDto.memo()))
            .status(Status.WAITING)
            .build();
  }

  public void verifyCanDelete(AppUser authUser) {
    verifyNotAccessOwner(authUser);
    verifyNotYetAccepted();
  }

  private void verifyNotAccessOwner(AppUser authUser) {
    if (this.menteeUserId != authUser.pid()) {
      throw new UnauthorizedAccessException();
    }
  }

  private void verifyNotAccessOwnerMentor(AppUser authUser) {
    if (this.mentorUserId != authUser.pid()) {
      throw new UnauthorizedAccessException();
    }
  }

  private void verifyNotYetAccepted() {
    if (this.status.equals(Status.ACCEPTED)) {
      throw new IllegalStateException("이미 승인 된 멘토 요청에 대해서는 삭제가 불가능합니다.");
    }
  }

  public void changeStatus(AppUser authUser, MentorRequestStatusDto requestStatus) {
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

  public long getMenteeUserId() {
    return menteeUserId;
  }

  public long getMentorUserId() {
    return mentorUserId;
  }

  public Status getStatus() {
    return status;
  }
}
