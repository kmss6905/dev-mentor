package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.devmentor.exception.schedule.ScheduleDetailContentValidateException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleMenteeMemo {

  @Column(name = "mentee_memo")
  private String content;

  // 생성시점에 반드시 content 가 존재하도록 강제한다.
  public ScheduleMenteeMemo(String content) {
    verifyMenteeMemoNotEmpty(content);
    this.content = content;
  }

  public static ScheduleMenteeMemo from(String content) {
    return new ScheduleMenteeMemo(content);
  }

  private void verifyMenteeMemoNotEmpty(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new ScheduleDetailContentValidateException("mentee memo can't be empty");
    }
  }
}
