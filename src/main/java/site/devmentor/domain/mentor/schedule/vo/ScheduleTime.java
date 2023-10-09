package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTime {

  @Column(name = "start_time", nullable = false)
  private LocalDateTime start;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime end;

  public ScheduleTime(LocalDateTime start, LocalDateTime end) {
    verifyNotNullStartAndEndTime(start, end);
    verifyStartTimeNotAfterEndTime(start, end);
    this.start = start;
    this.end = end;
  }

  private static void verifyNotNullStartAndEndTime(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("schedule require endTime");
    }
  }

  private void verifyStartTimeNotAfterEndTime(LocalDateTime startTime, LocalDateTime endTime) {
    if (startTime.isAfter(endTime)) {
      throw new IllegalArgumentException("startTime cannot be after endTime");
    }
  }

  public void update(LocalDateTime startDate, LocalDateTime endDate) {
    verifyNotNullStartAndEndTime(startDate, endDate);
    verifyStartTimeNotAfterEndTime(startDate, endDate);

    if (!this.start.equals(startDate)) {
      this.start = startDate;
    }

    if (!this.end.equals(endDate)) {
      this.end = endDate;
    }
  }
}
