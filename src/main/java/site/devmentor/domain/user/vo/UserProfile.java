package site.devmentor.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

  @Column(columnDefinition = "MEDIUMTEXT")
  private String content;

  public UserProfile(String content) {
    verifyContentNotNullOrEmpty(content);
    this.content  = content;
  }

  public void update(String content) {
    // 이전과 다른 content 일 경우애만 update
    if (!Objects.equals(this.content, content)) {
      this.content = content;
    }
  }

  private void verifyContentNotNullOrEmpty(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("content is cant not be null or empty");
    }
  }
}
