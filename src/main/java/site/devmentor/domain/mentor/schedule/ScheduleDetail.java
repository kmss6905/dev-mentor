package site.devmentor.domain.mentor.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.schedule.vo.*;
import site.devmentor.exception.schedule.ScheduleDetailContentValidateException;

import java.time.LocalDateTime;

@Table(name = "MENTORING_SCHEDULE_DETAIL")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class ScheduleDetail extends BaseEntity {

  @Size(max = 255)
  @NotNull
  @Column(name = "title", nullable = false)
  private String title;

  @Enumerated
  private ScheduleDetailTime time;

  @Enumerated
  private ScheduleDetailMemo memo;

  @Column(name = "status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ScheduleDetailStatus status = ScheduleDetailStatus.PENDING;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "is_deleted")
  private boolean isDeleted = false;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @Builder
  private ScheduleDetail(final String title, final ScheduleDetailMemo memo, final ScheduleDetailTime time, final Schedule schedule) {
    verifyMemoNotNull(memo);
    verifyTitleNotNull(title);
    verifyTimeNotNull(time);
    this.memo = memo;
    this.title = title;
    this.time = time;
    this.schedule = schedule;
  }

  private void verifyTimeNotNull(ScheduleDetailTime time) {
    if (time == null) {
      throw new ScheduleDetailContentValidateException("time can't not be null");
    }
  }

  private void verifyMemoNotNull(ScheduleDetailMemo memo) {
    if (memo == null) {
      throw new ScheduleDetailContentValidateException("memo can't not be null");
    }
  }

  private void verifyTitleNotNull(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new ScheduleDetailContentValidateException("title can't not be empty");
    }
  }

  public void update(ScheduleDetail detail) {
    this.memo = detail.memo;
    this.time = detail.time;
    this.title = detail.title;
  }

   public boolean isScheduleAuthor(AppUser appUser) {
     return this.schedule.isAuthorOf(appUser);
  }

  public String getTitle() {
    return title;
  }

  public ScheduleDetailTime getTime() {
    return time;
  }

  public ScheduleDetailMemo getMemo() {
    return memo;
  }

  public ScheduleDetailStatus getStatus() {
    return status;
  }
}
