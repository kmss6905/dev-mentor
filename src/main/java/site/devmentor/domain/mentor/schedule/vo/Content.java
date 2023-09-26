package site.devmentor.domain.mentor.schedule.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "memo", nullable = false)
  private String memo;

  public Content(String title, String memo) {
    if (title == null || memo == null) {
      throw new IllegalArgumentException("title, memo can't be null");
    }
    this.title = title;
    this.memo = memo;
  }
}
