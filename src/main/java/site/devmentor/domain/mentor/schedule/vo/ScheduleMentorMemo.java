package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleMentorMemo {

  @Column(name = "mentor_memo")
  private String content;

  public static ScheduleMentorMemo from(String content) {
    ScheduleMentorMemo scheduleMentorMemo = new ScheduleMentorMemo();
    scheduleMentorMemo.content = content;
    return scheduleMentorMemo;
  }
}
