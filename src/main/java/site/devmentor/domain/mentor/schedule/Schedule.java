package site.devmentor.domain.mentor.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.Status;
import site.devmentor.domain.mentor.schedule.vo.Content;
import site.devmentor.domain.mentor.schedule.vo.ScheduleTime;
import site.devmentor.domain.user.User;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;
import site.devmentor.exception.UnauthorizedAccessException;

import java.time.LocalDateTime;

@Table(name = "MENTORING_SCHEDULE")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Schedule extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "mentee_id")
  private User mentee;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "mentor_id")
  private User mentor;
  @Embedded
  private Content content;

  @Embedded
  private ScheduleTime time;

  @NotNull
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "request_id", nullable = false)
  private MentorRequest request;

  @Builder
  private Schedule(final User mentor, final User mentee, final MentorRequest request, final LocalDateTime startTime, final LocalDateTime endTime, final String title, final String memo) {
    verifyRequestNotNull(request);
    verifyOnlyForAcceptedRequest(request);
    verifyMentorAndMentee(mentor, mentee);
    verifyOwner(request, mentor);
    this.mentor = mentor;
    this.mentee = mentee;
    this.request = request;
    this.content = new Content(title, memo);
    this.time = new ScheduleTime(startTime, endTime);
  }

  private void verifyOwner(MentorRequest request, User mentor) {
    if (request.getToUserId() != mentor.getId()) {
      throw new UnauthorizedAccessException();
    }
  }


  private void verifyOnlyForAcceptedRequest(MentorRequest request) {
    if(!request.getStatus().equals(Status.ACCEPTED)) {
      throw new IllegalArgumentException();
    }
  }

  private void verifyMentorAndMentee(User mentor, User mentee) {
    if (mentee == null || mentor == null) {
      throw new IllegalArgumentException("schedule require mentor and mentee");
    }

    if (mentee.same(mentor)) {
      throw new IllegalArgumentException("can't same mentor and mentee");
    }
  }

  private void verifyRequestNotNull(MentorRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("schedule require request information");
    }
  }

  public static Schedule create(
          final User mentor, final User mentee, final MentorRequest request, final MentorScheduleDto mentorScheduleDto
          ) {
    return Schedule.builder()
            .mentee(mentee)
            .mentor(mentor)
            .startTime(mentorScheduleDto.getStartDate())
            .endTime(mentorScheduleDto.getEndDate())
            .title(mentorScheduleDto.getTitle())
            .memo(mentorScheduleDto.getMemo())
            .request(request).build();
  }

  public void update(AuthenticatedUser authUser, MentorScheduleUpdateDto mentorScheduleUpdateDto) {
    checkOwner(authUser);
    this.content.update(mentorScheduleUpdateDto.getTitle(), mentorScheduleUpdateDto.getMemo());
    this.time.update(mentorScheduleUpdateDto.getStartDate(), mentorScheduleUpdateDto.getEndDate());
  }

  private void checkOwner(AuthenticatedUser authUser) {
    if (this.mentor.getId() != authUser.userPid()) {
      throw new UnauthorizedAccessException();
    }
  }
}
