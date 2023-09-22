package site.devmentor.domain.mentor.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.dto.mentor.MentorRequestDto;

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
  protected MentorRequest(final long fromUserId, final long toUserId, final Memo memo) {
    this.memo = memo;
    this.toUserId = toUserId;
    this.fromUserId = fromUserId;
  }

  public static MentorRequest create(AuthenticatedUser authUser, MentorRequestDto mentorRequestDto) {
    return MentorRequest.builder()
            .fromUserId(authUser.userPid())
            .toUserId(mentorRequestDto.mentorUserId())
            .memo(new Memo(mentorRequestDto.memo()))
            .build();
  }
}
