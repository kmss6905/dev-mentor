package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.devmentor.exception.schedule.ScheduleDetailContentValidateException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDetailMemo {

  @Enumerated
  private ScheduleMenteeMemo toMenteeMemo;

  @Enumerated
  private ScheduleMentorMemo toMentorMemo;

  // ScheduleMenteeMemo 를 생성자 파라미터로 받기 전, ScheduleMenteeMemo 인스턴스 생성 시점에 content 에 대한 유효성 검증을 거친다.
  // 그렇기 때문에 menteeMemo 에 대한 content 에 대한 유효성 검증을 ScheduleDetailMemo 를 만들 시점에는 할 필요 없다고 생각
  // ScheduleMenteeMemo 에 대한 도메인 규칙(content 가 반드시 있어야 한다.)
  public ScheduleDetailMemo(ScheduleMenteeMemo menteeMemo, ScheduleMentorMemo mentorMemo) {
    if (menteeMemo == null) {
      throw new ScheduleDetailContentValidateException("mentee memo can't be null");
    }
    this.toMenteeMemo = menteeMemo;
    this.toMentorMemo = mentorMemo;
  }
  public static ScheduleDetailMemo create(final ScheduleMentorMemo mentorMemo, final ScheduleMenteeMemo menteeMemo) {
    return new ScheduleDetailMemo(menteeMemo, mentorMemo);
  }
}
