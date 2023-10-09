package site.devmentor.domain.mentor.schedule;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.Status;
import site.devmentor.domain.mentor.schedule.vo.Content;
import site.devmentor.domain.mentor.schedule.vo.ScheduleDetailTime;
import site.devmentor.domain.mentor.schedule.vo.ScheduleTime;
import site.devmentor.domain.user.User;
import site.devmentor.dto.mentor.schedule.MentorScheduleDto;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;
import site.devmentor.exception.UnauthorizedAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "MENTORING_SCHEDULE")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE MENTORING_SCHEDULE SET is_deleted = true, deleted_at = now()")
@Where(clause = "is_deleted=false")
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

  @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
  @JoinColumn(name = "id", referencedColumnName = "id")
  private List<ScheduleDetail> details = new ArrayList<>();

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

  public static Schedule create(final User mentor, final User mentee, final MentorRequest request, final MentorScheduleDto mentorScheduleDto) {
    return Schedule.builder()
            .mentee(mentee)
            .mentor(mentor)
            .startTime(mentorScheduleDto.startDate())
            .endTime(mentorScheduleDto.endDate())
            .title(mentorScheduleDto.title())
            .memo(mentorScheduleDto.memo())
            .request(request).build();
  }

  public void update(AuthenticatedUser authUser, MentorScheduleUpdateDto mentorScheduleUpdateDto) {
    checkOwner(authUser);
    this.content.update(mentorScheduleUpdateDto.title(), mentorScheduleUpdateDto.memo());
    this.time.update(mentorScheduleUpdateDto.startDate(), mentorScheduleUpdateDto.endDate());
  }

  private void checkOwner(AuthenticatedUser authUser) {
    if (this.mentor.getId() != authUser.userPid()) {
      throw new UnauthorizedAccessException();
    }
  }

  public void delete(AuthenticatedUser authUser) {
    checkOwner(authUser);
    this.deletedAt = LocalDateTime.now();
    this.isDeleted = true;
  }

  public void addDetail(ScheduleDetail scheduleDetail, AuthenticatedUser authUser) {
    checkOwner(authUser);
    verifyDetailTimeBetweenScheduleTime(scheduleDetail.getTime());
    this.details.add(scheduleDetail);
  }

  private void verifyDetailTimeBetweenScheduleTime(ScheduleDetailTime detailTime) {
    if (isStartTimeNotInRange(detailTime)) {
      throw new IllegalArgumentException("세부 스케줄 시작시간은 스케쥴 시간 안에 있어야합니다.");
    }

    if (isEndTimeNotInRange(detailTime)) {
      throw new IllegalArgumentException("세부 스케줄 종료시간은 스케쥴 시간 안에 있어야합니다.");
    }
  }

  private boolean isStartTimeNotInRange(ScheduleDetailTime detailTime) {
    return detailTime.getStartTime().isBefore(this.time.getStart()) || detailTime.getStartTime().isAfter(this.time.getEnd());
  }

  private boolean isEndTimeNotInRange(ScheduleDetailTime detailTime) {
    return detailTime.getEndTime().isAfter(this.time.getEnd()) || detailTime.getEndTime().isBefore(this.time.getStart());
  }

  public List<ScheduleDetail> getDetails() {
    return details;
  }

  public Content getContent() {
    return content;
  }

  public ScheduleTime getTime() {
    return time;
  }
}
