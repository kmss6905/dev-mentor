package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDetailTime {

  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  private ScheduleDetailTime(LocalDateTime startTime, LocalDateTime endTime) {
    if (!startTime.isBefore(endTime)) {
      throw new IllegalArgumentException("시작시간은 종료시간보다 앞서 있어야합니다.");
    }
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static ScheduleDetailTime create(LocalDateTime startTime, LocalDateTime endTime) {
    return new ScheduleDetailTime(startTime, endTime);
  }
}
