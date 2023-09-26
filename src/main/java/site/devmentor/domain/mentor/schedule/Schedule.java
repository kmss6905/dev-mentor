package site.devmentor.domain.mentor.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.Status;
import site.devmentor.domain.mentor.schedule.vo.Content;
import site.devmentor.domain.user.User;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;

import java.time.LocalDateTime;

@Table(name = "MENTORING_SCHEDULE")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

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
    verifyTime(startTime, endTime);
    verifyMentorAndMentee(mentor, mentee);
    this.mentor = mentor;
    this.mentee = mentee;
    this.request = request;
    this.startTime = startTime;
    this.endTime = endTime;
    this.content = new Content(title, memo);
  }

  private void verifyTime(LocalDateTime startTime, LocalDateTime endTime) {
    verifyTimeNotNull(startTime);
    verifyTimeNotNull(endTime);
    verifyStartTimeNotAfterEndTime(startTime, endTime);
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

  private static void verifyTimeNotNull(LocalDateTime endTime) {
    if (endTime == null) {
      throw new IllegalArgumentException("schedule require endTime");
    }
  }

  private void verifyStartTimeNotAfterEndTime(LocalDateTime startTime, LocalDateTime endTime) {
    if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
      throw new IllegalArgumentException("startTime cannot be after endTime");
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
}
