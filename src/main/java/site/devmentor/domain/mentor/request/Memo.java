package site.devmentor.domain.mentor.request;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

  private String memo;
  public Memo(String memo) {
    verifyMemoIsNotEmpty(memo);
    this.memo = memo;
  }

  private void verifyMemoIsNotEmpty(String memo) {
    if (memo == null || memo.trim().isEmpty()) {
      throw new IllegalArgumentException();
    }
  }
}
