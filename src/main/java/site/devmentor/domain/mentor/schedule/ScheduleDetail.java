package site.devmentor.domain.mentor.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.schedule.vo.*;
import site.devmentor.dto.mentor.schedule.MentorScheduleDetailDto;
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

  @Builder
  private ScheduleDetail(final String title, final ScheduleDetailMemo memo, final ScheduleDetailTime time) {
    verifyTitleNotEmpty(title);
    verifyMemoNotNull(memo);
    verifyTimeNotNull(time);
    this.memo = memo;
    this.title = title;
    this.time = time;
    this.status = ScheduleDetailStatus.PENDING;
  }

  public static ScheduleDetail create(MentorScheduleDetailDto dto) {
    ScheduleMenteeMemo menteeMemo = ScheduleMenteeMemo.from(dto.menteeMemo());
    ScheduleMentorMemo mentorMemo = ScheduleMentorMemo.from(dto.mentorMemo());
    ScheduleDetailMemo detailMemo = ScheduleDetailMemo.create(mentorMemo, menteeMemo);
    ScheduleDetailTime detailTime = ScheduleDetailTime.create(dto.startDate(), dto.endDate());
    return ScheduleDetail.builder()
            .memo(detailMemo)
            .time(detailTime)
            .title(dto.title())
            .build();
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

  private void verifyTitleNotEmpty(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new ScheduleDetailContentValidateException("title can't not be empty");
    }
  }

  public ScheduleDetailTime getTime() {
    return time;
  }
}
