package site.devmentor.dto.mentor.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MentorScheduleUpdateDto {
  @NotNull(message = "[title] 은 필수 입니다.")
  private String title;

  @NotNull(message = "[memo] 은 필수 입니다.")
  private String memo;

  @NotNull(message = "[startDate] 은 필수 입니다.")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startDate;

  @NotNull(message = "[endDate] 은 필수 입니다.")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endDate;
}
